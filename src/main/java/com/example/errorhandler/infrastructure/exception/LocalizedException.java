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
