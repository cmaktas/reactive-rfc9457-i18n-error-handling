package com.example.errorhandler.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(description = "Request object demonstrating various validation constraints for testing purposes. " +
        "Each field has specific constraints, and example values are provided to help users trigger validation errors.")
public class TestRequest {

    @NotNull(message = "{validation.exception.not_null}")
    @NotBlank(message = "{validation.exception.not_blank}")
    @Schema(description = "A non-null and non-blank string", example = "example")
    private String fieldNotBlank;

    @NotNull(message = "{validation.exception.not_null}")
    @Email(message = "{validation.exception.email}")
    @Schema(description = "A valid email address", example = "user@example.com")
    private String fieldEmail;

    @NotNull(message = "{validation.exception.not_null}")
    @Positive(message = "{validation.exception.positive}")
    @Schema(description = "A positive integer", example = "10")
    private Integer fieldPositive;

    @NotNull(message = "{validation.exception.not_null}")
    @PositiveOrZero(message = "{validation.exception.positive_or_zero}")
    @Schema(description = "A positive integer or zero", example = "0")
    private Integer fieldPositiveOrZero;

    @NotNull(message = "{validation.exception.not_null}")
    @Negative(message = "{validation.exception.negative}")
    @Schema(description = "A negative integer", example = "-5")
    private Integer fieldNegative;

    @NotNull(message = "{validation.exception.not_null}")
    @NegativeOrZero(message = "{validation.exception.negative_or_zero}")
    @Schema(description = "A negative integer or zero", example = "0")
    private Integer fieldNegativeOrZero;

    @NotNull(message = "{validation.exception.not_null}")
    @Min(value = 10, message = "{validation.exception.min}")
    @Max(value = 100, message = "{validation.exception.max}")
    @Schema(description = "An integer within the range [10, 100]", example = "50")
    private Integer fieldRange;

    @NotNull(message = "{validation.exception.not_null}")
    @Size(min = 3, max = 10, message = "{validation.exception.size}")
    @Schema(description = "A string with length between 3 and 10 characters", example = "test123")
    private String fieldSize;

    @NotNull(message = "{validation.exception.not_null}")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "{validation.exception.pattern}")
    @Schema(description = "An alphanumeric string (letters and numbers only)", example = "abc123")
    private String fieldPattern;

    @NotNull(message = "{validation.exception.not_null}")
    @Past(message = "{validation.exception.past}")
    @Schema(description = "A date in the past", example = "2000-01-01", type = "string", format = "date")
    private LocalDate fieldPast;

    @NotNull(message = "{validation.exception.not_null}")
    @Future(message = "{validation.exception.future}")
    @Schema(description = "A date in the future", example = "2100-01-01", type = "string", format = "date")
    private LocalDate fieldFuture;

    @NotNull(message = "{validation.exception.not_null}")
    @FutureOrPresent(message = "{validation.exception.future_or_present}")
    @Schema(description = "A date in the future or today", example = "2100-01-01", type = "string", format = "date")
    private LocalDate fieldFutureOrPresent;

    @NotNull(message = "{validation.exception.not_null}")
    @PastOrPresent(message = "{validation.exception.past_or_present}")
    @Schema(description = "A date in the past or today", example = "2020-01-01", type = "string", format = "date")
    private LocalDate fieldPastOrPresent;

    @NotNull(message = "{validation.exception.not_null}")
    @Digits(integer = 5, fraction = 2, message = "{validation.exception.digits}")
    @Schema(description = "A decimal number with up to 5 digits before the decimal and 2 after", example = "12345.67")
    private Double fieldDigits;

    @NotNull(message = "{validation.exception.not_null}")
    @Pattern(regexp = "\\+?[0-9]{10,15}", message = "{validation.exception.phone_number}")
    @Schema(description = "A phone number with 10 to 15 digits, optionally starting with '+'", example = "+1234567890")
    private String fieldPhoneNumber;
}
