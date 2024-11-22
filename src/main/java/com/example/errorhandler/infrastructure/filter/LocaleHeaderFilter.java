package com.example.errorhandler.infrastructure.filter;

import com.example.errorhandler.domain.constants.ContextConstants;
import com.example.errorhandler.infrastructure.config.LocaleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

/**
 * A web filter that processes the "Accept-Language" HTTP header to determine the user's locale.
 * It resolves the locale and stores it in the {@link ServerWebExchange} attributes for later use
 * in internationalization (i18n) of error messages and other localized content.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LocaleHeaderFilter implements WebFilter {

    private final LocaleConfig localeConfig;

    /**
     * Filters each incoming HTTP request to resolve the user's locale based on the "Accept-Language" header.
     * The resolved locale is stored in the reactor context under the key defined by
     * {@link ContextConstants#ACCEPT_LANGUAGE_CONTEXT_KEY}.
     *
     * @param exchange the current server exchange
     * @param chain    the web filter chain to delegate to
     * @return a {@link Mono} that indicates filter chain completion
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return resolveLocale(exchange.getRequest().getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE))
                .flatMap(resolvedLocale ->
                        chain.filter(exchange)
                                .contextWrite(ctx -> ctx.put(ContextConstants.ACCEPT_LANGUAGE_CONTEXT_KEY, resolvedLocale))
                );
    }

    /**
     * Resolves the locale based on the provided "Accept-Language" header value.
     * If the header is missing, empty, invalid, or specifies an unsupported locale,
     * it defaults to {@link Locale#ENGLISH}.
     *
     * @param acceptLanguage the value of the "Accept-Language" header
     * @return a {@link Mono} emitting the resolved {@link Locale}
     */
    private Mono<Locale> resolveLocale(String acceptLanguage) {
        return Mono.justOrEmpty(acceptLanguage)
                .filter(lang -> !lang.isBlank())
                .flatMap(lang -> {
                    try {
                        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(lang);
                        Locale lookupLocale = Locale.lookup(languageRanges, localeConfig.getSupportedLocales());
                        return Mono.justOrEmpty(lookupLocale)
                                .map(locale -> Locale.forLanguageTag(locale.getLanguage()))
                                .defaultIfEmpty(Locale.ENGLISH);
                    } catch (IllegalArgumentException ex) {
                        log.warn("Invalid Accept-Language header: {}. Falling back to default locale.", lang, ex);
                        return Mono.just(Locale.ENGLISH);
                    }
                })
                .defaultIfEmpty(Locale.ENGLISH);
    }
}

