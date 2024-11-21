package com.example.errorhandler.infrastructure.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Global exception handler for the application.
 * This class handles exceptions thrown during the execution of the application
 * and provides appropriate responses with standardized error messages,
 * following the RFC 9457 standard for error responses.
 * It specifically handles:
 * - {@link LocalizedException}: Custom business exceptions with localized messages.
 * - {@link WebExchangeBindException}: Exceptions thrown during validation failures.
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handles custom business exceptions of type {@link LocalizedException}.
     * Retrieves localized error messages and constructs a {@link ProblemDetail}
     * response accordingly.
     *
     * @param ex the thrown {@link LocalizedException}
     * @return a {@link Mono} emitting the {@link ProblemDetail} response
     */
    @ExceptionHandler(LocalizedException.class)
    public Mono<ProblemDetail> handleBusinessException(LocalizedException ex) {
        return ex.getLocalizedMessage(messageSource)
                .flatMap(localizedMessageAndTitle -> {
                    HttpStatus status = ex.getStatus();
                    ProblemDetail problemDetail = new ProblemDetailBuilder(status, localizedMessageAndTitle.get("message"))
                            .title(localizedMessageAndTitle.get("title"))
                            .build();
                    return Mono.just(problemDetail);
                });
    }

    /**
     * Handles validation exceptions of type {@link WebExchangeBindException}.
     * Extracts field errors and constructs a {@link ProblemDetail} response with
     * detailed validation errors.
     *
     * @param ex the thrown {@link WebExchangeBindException}
     * @return a {@link Mono} emitting the {@link ProblemDetail} response
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ProblemDetail> handleValidationException(WebExchangeBindException ex) {
        log.info("Handling WebExchangeBindException: {}", ex.getMessage());
        List<ApiErrorDetails> errors = ex.getFieldErrors().stream()
                .map(fieldError -> new ApiErrorDetails(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        ProblemDetail problemDetail = new ProblemDetailBuilder(BAD_REQUEST, "Validation failed.")
                .title("Validation Error")
                .errors(errors)
                .build();
        return Mono.just(problemDetail);
    }

}
