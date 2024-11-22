package com.example.errorhandler.infrastructure.exception;


import com.example.errorhandler.domain.constants.ContextConstants;
import com.example.errorhandler.domain.enums.LocalizedExceptionTypes;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;

/**
 * A custom runtime exception that supports internationalization (i18n) by providing
 * localized error messages and titles based on the user's locale. This exception is
 * designed for use in reactive Spring WebFlux applications and leverages Reactor's
 * context to propagate locale information throughout the reactive chain.
 * When thrown, this exception can generate localized error messages using a
 * {@link MessageSource}, and it supports fallback mechanisms in case a localized
 * message is not found for the specified locale.
 */
@Slf4j
public class LocalizedException extends RuntimeException {

    private final String errorMessageKey;
    private final String errorTitleKey;
    private final Object[] args;
    @Getter
    private final HttpStatus status;

    public LocalizedException(LocalizedExceptionTypes localizedExceptionTypes, Object... args) {
        this.errorMessageKey = localizedExceptionTypes.getErrorMessageKey();
        this.errorTitleKey = localizedExceptionTypes.getErrorTitleKey();
        this.status = localizedExceptionTypes.getStatus();
        this.args = args;
    }

    /**
     * Retrieves a {@link Mono} that emits a map containing the localized error message and title.
     * The method uses the Reactor context to obtain the user's locale, which is expected to be
     * stored under the key defined in {@link ContextConstants#ACCEPT_LANGUAGE_CONTEXT_KEY}.
     * If the locale is not found in the context, it defaults to {@link Locale#ENGLISH}.
     * The method attempts to retrieve the localized messages from the provided {@link MessageSource}.
     * If the messages are not found for the user's locale, it falls back to English. If they are
     * still not found, default messages are used.
     * @param messageSource The {@link MessageSource} used to retrieve localized messages.
     * @return A {@link Mono} emitting a {@link Map} containing the localized "message" and "title".
     */
    public Mono<Map<String, String>> getLocalizedMessage(MessageSource messageSource) {
        return Mono.deferContextual(contextView -> {
            Locale locale = contextView.getOrDefault(ContextConstants.ACCEPT_LANGUAGE_CONTEXT_KEY, Locale.ENGLISH);
            String localizedMessage;
            String localizedTitle;
            try {
                localizedMessage = messageSource.getMessage(errorMessageKey, args, locale);
                localizedTitle = messageSource.getMessage(errorTitleKey, args, locale);
            } catch (NoSuchMessageException exception) {
                log.error("Can't find the localized message for errorMessageKey: {} and for the locale: {}. Falling back to English locale.", errorMessageKey, locale);
                try {
                    localizedMessage = messageSource.getMessage(errorMessageKey, args, Locale.ENGLISH);
                    localizedTitle = messageSource.getMessage(errorTitleKey, args, Locale.ENGLISH);
                } catch (NoSuchMessageException englishFallbackException) {
                    log.error("Can't find the English fallback message for errorMessageKey: {}. Returning default message.", errorMessageKey);
                    localizedMessage = "An unexpected error occurred.";
                    localizedTitle = "Error";
                }
            }
            Map<String, String> localizedMessages = Map.of(
                "message", localizedMessage,
                "title", localizedTitle
            );
            return Mono.just(localizedMessages);
        });
    }

}
