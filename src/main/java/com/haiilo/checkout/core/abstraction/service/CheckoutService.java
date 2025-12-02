package com.haiilo.checkout.core.abstraction.service;

import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.CheckoutResult;

public interface CheckoutService {
    CheckoutResult processCheckout(CheckoutRequest request);
}
