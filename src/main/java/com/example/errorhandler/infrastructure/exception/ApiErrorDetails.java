package com.example.errorhandler.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class ApiErrorDetails implements Serializable {
    private final String pointer;
    private final String reason;
}
