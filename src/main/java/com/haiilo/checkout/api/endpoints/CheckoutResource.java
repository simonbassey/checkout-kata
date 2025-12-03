package com.haiilo.checkout.api.endpoints;

import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.CheckoutResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@Tag(name = "Checkout", description = "Checkout API for calculating shopping cart totals with promotional offers")
public interface CheckoutResource {


    @Operation(summary = "Calculate checkout total")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully calculated checkout total",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CheckoutResult.class),
                examples = @ExampleObject(
                    name = "Successful calculation",
                    value = """
                        {
                          "totalPrice": 175.50
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - validation failed or malformed JSON",
            content = @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class),
                examples = {
                    @ExampleObject(
                        name = "Empty items list",
                        value = """
                            {
                              "type": "about:blank",
                              "title": "Invalid Request",
                              "status": 400,
                              "detail": "Validation failed",
                              "errors": ["items: must not be empty"]
                            }
                            """
                    ),
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                schema = @Schema(implementation = ProblemDetail.class),
                examples = @ExampleObject(
                    name = "Server error",
                    value = """
                        {
                          "type": "about:blank",
                          "title": "Internal Server Error",
                          "status": 500,
                          "detail": "An unexpected error occurred"
                        }
                        """
                )
            )
        )
    })
    @PostMapping("calculate-total")
    ResponseEntity<CheckoutResult> calculateTotal(@RequestBody @Valid CheckoutRequest checkoutRequest);
}
