package com.haiilo.checkout.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.haiilo.checkout.core.abstraction.service.PriceCalculationService;
import com.haiilo.checkout.core.domain.model.CheckoutRequest;
import com.haiilo.checkout.core.domain.model.CheckoutResult;
import com.haiilo.checkout.core.domain.model.LineItem;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    @Mock
    private PriceCalculationService priceCalculationService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void shouldProcessCheckoutAndReturnTotalPrice() {
        CheckoutRequest request = new CheckoutRequest();
        List<LineItem> items = Arrays.asList(
            new LineItem("Apple", 2),
            new LineItem("Banana", 3)
        );
        request.setItems(items);

        BigDecimal expectedTotal = new BigDecimal("150.00");
        when(priceCalculationService.calculateTotalPrice(items)).thenReturn(expectedTotal);

        CheckoutResult result = checkoutService.processCheckout(request);

        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotal);
        verify(priceCalculationService).calculateTotalPrice(items);
    }

    @Test
    void shouldDelegateCalculationToPriceCalculationService() {
        CheckoutRequest request = new CheckoutRequest();
        List<LineItem> items = List.of(new LineItem("Apple", 5));
        request.setItems(items);

        when(priceCalculationService.calculateTotalPrice(anyList()))
            .thenReturn(BigDecimal.valueOf(120));

        CheckoutResult result = checkoutService.processCheckout(request);

        assertThat(result.totalPrice()).isEqualByComparingTo("120");
        verify(priceCalculationService).calculateTotalPrice(items);
    }
}
