package com.haiilo.checkout.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.haiilo.checkout.core.abstraction.repository.PriceRepository;
import com.haiilo.checkout.core.abstraction.service.PriceCalculationService;
import com.haiilo.checkout.core.domain.exception.ItemPriceNotFoundException;
import com.haiilo.checkout.core.domain.model.LineItem;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PriceCalculationServiceIntTest {

    @Autowired
    private PriceRepository priceRepository;

    private PriceCalculationService priceCalculationService;

    @BeforeEach
    void setUp() {
        priceCalculationService = new PriceCalculationServiceImpl(priceRepository);
    }

    @Test
    void shouldCalculateTotalPriceFromDatabase() {

        // Given
        LineItem appleItem = new LineItem("Apple", 3); // Apples: 2 for 45 + 1 for 30 = 75
        LineItem bananaItem = new LineItem("Banana", 5); // Bananas: 3 for 130 + 2 for 100 = 230

        // When
        BigDecimal total = priceCalculationService.calculateTotalPrice(
            Arrays.asList(appleItem, bananaItem)
        );

        // Then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(305));
    }

    @Test
    void shouldRetrievePricesFromDatabaseCaseInsensitively() {
        //Given
        LineItem appleItem = new LineItem("APPLE", 2);

        // When
        BigDecimal total = priceCalculationService.calculateTotalPrice(List.of(appleItem)); // apple:30, 2 for 45

        // Then
        assertThat(total).isEqualByComparingTo("45");
    }

    @Test
    void shouldCalculatePriceForItemWithoutOffer() {
        // Given
        LineItem peachItem = new LineItem("Peach", 2);

        // When
        BigDecimal total = priceCalculationService.calculateTotalPrice(List.of(peachItem));

        // Then
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(120));
    }

    @Test
    void shouldThrowExceptionForNonExistentItem() {
        LineItem unknownItem = new LineItem("NonExistent", 1);

        assertThatThrownBy(() -> priceCalculationService.calculateTotalPrice(List.of(unknownItem)))
            .isInstanceOf(ItemPriceNotFoundException.class)
            .hasMessageContaining("No price found for item with name nonexistent");
    }

    @Test
    void shouldHandleComplexCheckoutScenario() {
        List<LineItem> items = Arrays.asList(
            new LineItem("Apple", 4),      // apple 30, 2 for 45
            new LineItem("Banana", 3),     // banana 50, 3 for 130
            new LineItem("Peach", 1),      // peach 60, no offer
            new LineItem("Mango", 5),      // mango 10, 2 for 15
            new LineItem("Kiwi", 2)        // kiwi 20, no offer
        );

        BigDecimal total = priceCalculationService.calculateTotalPrice(items);

        assertThat(total).isEqualByComparingTo("360");
    }
}
