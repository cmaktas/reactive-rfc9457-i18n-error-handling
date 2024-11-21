package com.example.errorhandler.infrastructure.exception;

import com.example.errorhandler.domain.constants.ExceptionConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProblemDetailBuilder {
    private final HttpStatus status;
    private final String detail;
    private String title;
    private List<ApiErrorDetails> errors;

    public ProblemDetailBuilder(HttpStatus status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    public ProblemDetailBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ProblemDetailBuilder errors(List<ApiErrorDetails> errors) {
        this.errors = errors;
        return this;
    }

    public ProblemDetail build() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        if (title != null) {
            problemDetail.setTitle(title);
        } else {
            problemDetail.setTitle(status.getReasonPhrase());
        }

        problemDetail.setType(URI.create(ExceptionConstants.MOZILLA_DEVELOPER_BASE_URI.toString() + status.value()));

        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(LocalDateTime.now());
        problemDetail.setProperty("timestamp", timestamp);

        if (errors != null && !errors.isEmpty()) {
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }
}
