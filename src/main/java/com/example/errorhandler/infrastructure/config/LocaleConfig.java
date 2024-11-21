package com.example.errorhandler.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class LocaleConfig {

    @Value("${supported-locales}")
    private String[] supportedLocales;

    public List<Locale> getSupportedLocales() {
        return Stream.of(supportedLocales)
            .map(Locale::forLanguageTag)
            .collect(Collectors.toList());
    }
}
