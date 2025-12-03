package com.haiilo.checkout.core.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.haiilo.checkout.core.abstraction.repository.PriceRepository;
import com.haiilo.checkout.core.domain.entity.Price;
import com.haiilo.checkout.core.domain.exception.ItemPriceNotFoundException;
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
class PriceCalculationServiceTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private PriceCalculationServiceImpl priceCalculationService;


    @Test
    void calculateTotalPrice_ShouldCorrectlyCalculatePriceForOneItemWithoutOffer() {
        // Given
        String itemCode = "Apple";
        double itemPrice = 50.0;
        Price price = createItemPrice(itemCode, itemPrice, null);

        when(priceRepository.findAllByCodeIn(any())).thenReturn(List.of(price));

        LineItem lineItem = new LineItem(itemCode, 2);
        List<LineItem> items = List.of(lineItem);

        // When
        BigDecimal totalPrice = priceCalculationService.calculateTotalPrice(items);

        // Then
        assertThat(totalPrice).isEqualByComparingTo(BigDecimal.valueOf(100.0));
    }

    @Test
    void calculateTotalPrice_ShouldCalculateTotalPriceForOneItemWithOffer() {
        // Given
        LineItem appleItem = new LineItem("Apple", 5);
        Price applePrice = createItemPrice("apple", 30, "2 for 45");

        when(priceRepository.findAllByCodeIn(any())).thenReturn(List.of(applePrice));

        // When

        BigDecimal total = priceCalculationService.calculateTotalPrice(List.of(appleItem));

        // Then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(120));
    }

    @Test
    void calculateTotalPrice_ShouldCalculateTotalPriceWithQuantityBelowOfferQuantity() {
        // Given
        LineItem bananaItem = new LineItem("Banana", 2);
        Price bananaPrice = createItemPrice("banana", 50, "3 for 130");

        when(priceRepository.findAllByCodeIn(any())).thenReturn(List.of(bananaPrice));

        // When
        BigDecimal total = priceCalculationService.calculateTotalPrice(List.of(bananaItem));

        // Then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void calculateTotalPrice_ShouldCorrectlyCalculateTotalPriceForMultipleItems() {
        // Given
        LineItem appleItem = new LineItem("Apple", 3);
        LineItem bananaItem = new LineItem("Banana", 2);

        Price applePrice = createItemPrice("apple", 30, "2 for 45");
        Price bananaPrice = createItemPrice("banana", 50, "");

        when(priceRepository.findAllByCodeIn(any()))
            .thenReturn(Arrays.asList(applePrice, bananaPrice));

        // When
        BigDecimal total = priceCalculationService.calculateTotalPrice(
            Arrays.asList(appleItem, bananaItem)
        );

        // Then
        assertThat(total).isEqualByComparingTo("175");
    }

    @Test
    void calculateTotalPrice_ShouldHandleCaseInsensitiveItemNames() {
        // Given
        LineItem appleItem1 = new LineItem("Apple", 1);
        LineItem appleItem2 = new LineItem("APPLE", 1);
        LineItem appleItem3 = new LineItem("apple", 1);

        Price applePrice = createItemPrice("apple", 30, "");

        when(priceRepository.findAllByCodeIn(any())).thenReturn(List.of(applePrice));

        // When
        BigDecimal total = priceCalculationService.calculateTotalPrice(
            Arrays.asList(appleItem1, appleItem2, appleItem3)
        );

        //Then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void shouldThrowExceptionWhenPriceNotFound() {
        LineItem unknownItem = new LineItem("peach", 1);

        when(priceRepository.findAllByCodeIn(any())).thenReturn(List.of());

        assertThatThrownBy(() -> priceCalculationService.calculateTotalPrice(List.of(unknownItem)))
            .isInstanceOf(ItemPriceNotFoundException.class)
            .hasMessageContaining("No price found for item with name peach");
    }

    @Test
    void calculateTotalPrice_ShouldCorrectlyHandleOddNumberOfItemsWithOffer() {
        // Given
        LineItem mangoItem = new LineItem("Mango", 5);
        Price mangoPrice = createItemPrice("mango", 10, "2 for 15");

        when(priceRepository.findAllByCodeIn(any())).thenReturn(List.of(mangoPrice));

        //When
        BigDecimal total = priceCalculationService.calculateTotalPrice(List.of(mangoItem));

        // Then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(40));
    }

    private Price createItemPrice(String itemCode, double price, String offer) {
        Price itemPrice = new Price();
        itemPrice.setCode(itemCode);
        itemPrice.setPrice(BigDecimal.valueOf(price));
        itemPrice.setOffer(offer);
        return itemPrice;
    }
}
