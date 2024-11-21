package com.example.errorhandler.web.controller;

import com.example.errorhandler.domain.enums.LocalizedExceptionTypes;
import com.example.errorhandler.infrastructure.exception.LocalizedException;
import com.example.errorhandler.web.model.TestRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class TestController {

    @Operation(
            summary = "Validate and process TestRequest",
            description = """
                    This endpoint accepts a `TestRequest` object and validates its fields based on the defined constraints.
                    The `Accept-Language` header determines the localization of error messages.
                    Additionally, if the `throwException` parameter is set to `true` and validation passes, a localized business exception will be thrown to demonstrate error handling.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Validation passed successfully."),
            @ApiResponse(responseCode = "400", description = "Validation failed for the provided request object."),
            @ApiResponse(responseCode = "409", description = "A business exception occurred."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/test")
    public Mono<ResponseEntity<Void>> test(
            @Parameter(
                    name = "Accept-Language",
                    description = """
                            Language for localized error messages. Supported locales: 'ar', 'fr', 'en', 'ru'. A null value or invalid locale will fall back to default locale.ENGLISH
                            """,
                    schema = @Schema(type = "string", allowableValues = {"ar", "fr", "en", "ru", "invalidLocale"})
            )
            @RequestHeader(name = "Accept-Language", required = false) String language,
            @Parameter(
                    name = "throwException",
                    description = """
                            If `true` and validation passes, a localized business exception will be thrown. 
                            Otherwise, a 200 OK response is returned.
                            """,
                    required = true,
                    example = "false"
            )
            @RequestParam(name = "throwException") boolean throwException,
            @Valid @RequestBody TestRequest testRequest) {
        if (throwException) {
            throw new LocalizedException(LocalizedExceptionTypes.DEFAULT_BUSINESS_ERROR, HttpStatus.CONFLICT);
        }
        return Mono.just(ResponseEntity.ok().build());
    }
}
