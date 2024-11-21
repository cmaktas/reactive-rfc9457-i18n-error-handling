package com.example.errorhandler.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum LocalizedExceptionTypes {

    DEFAULT_BUSINESS_ERROR("business.exception.default.message", "business.exception.default.title", HttpStatus.PRECONDITION_REQUIRED);

    private final String errorMessageKey;
    private final String errorTitleKey;
    private final HttpStatus status;

}
