package com.haiilo.checkout.api;

import com.haiilo.checkout.core.abstraction.service.CheckoutService;
import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.CheckoutResult;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@AllArgsConstructor
public class CheckoutResource {

    private final CheckoutService checkoutService;

    @PostMapping("calculate-total")
    public ResponseEntity<CheckoutResult> calculateTotal(@RequestBody @Valid CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(checkoutService.processCheckout(checkoutRequest));
    }
}
