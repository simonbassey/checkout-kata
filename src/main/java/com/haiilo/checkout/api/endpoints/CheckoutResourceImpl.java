package com.haiilo.checkout.api.endpoints;

import com.haiilo.checkout.core.abstraction.service.CheckoutService;
import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.CheckoutResult;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CheckoutResourceImpl implements CheckoutResource {

    private final CheckoutService checkoutService;

    public ResponseEntity<CheckoutResult> calculateTotal(CheckoutRequest checkoutRequest) {
        return ResponseEntity.ok(checkoutService.processCheckout(checkoutRequest));
    }
}
