package com.haiilo.checkout.core.service;

import com.haiilo.checkout.core.abstraction.service.CheckoutService;
import com.haiilo.checkout.core.abstraction.service.PriceCalculationService;
import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.CheckoutResult;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class CheckoutServiceImpl implements CheckoutService {

    private final PriceCalculationService priceCalculationService;

    @Override
    public CheckoutResult processCheckout(CheckoutRequest request) {
        BigDecimal totalPrice = priceCalculationService.calculateTotalPrice(request.getItems());
        return new CheckoutResult(totalPrice);
    }
}
