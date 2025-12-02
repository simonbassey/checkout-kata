package com.haiilo.checkout.core.abstraction.service;

import com.haiilo.checkout.core.domain.model.LineItem;
import java.math.BigDecimal;
import java.util.List;

public interface PriceCalculationService {
    BigDecimal calculateTotalPrice(List<LineItem> items);
}
