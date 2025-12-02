package com.haiilo.checkout.core.service;

import com.haiilo.checkout.core.abstraction.repository.PriceRepository;
import com.haiilo.checkout.core.abstraction.service.PriceCalculationService;
import com.haiilo.checkout.core.domain.entity.Price;
import com.haiilo.checkout.core.domain.exception.ItemPriceNotFoundException;
import com.haiilo.checkout.core.domain.model.LineItem;
import com.haiilo.checkout.core.domain.model.PriceOffer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PriceCalculationServiceImpl implements PriceCalculationService {

    private final PriceRepository priceRepository;

    @Override
    public BigDecimal calculateTotalPrice(List<LineItem> items) {
        Map<String, List<LineItem>> itemGroups = groupCartItemsByName(items);
        Map<String, Price> itemCodeToPriceMap = fetchPricesForItemByNames(itemGroups.keySet());

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (String itemName : itemGroups.keySet()) {
            if (!itemCodeToPriceMap.containsKey(itemName)) {
                throw new ItemPriceNotFoundException(itemName);
            }
            Price itemPrice = itemCodeToPriceMap.get(itemName);
            List<LineItem> lineItems = itemGroups.get(itemName);
            totalPrice = totalPrice.add(calculateTotalPriceForItemGroup(lineItems, itemPrice));
        }
        return totalPrice;
    }

    private Map<String, List<LineItem>> groupCartItemsByName(List<LineItem> items) {
        return items.stream().collect(
            Collectors.groupingBy(item -> item.itemName().trim().toLowerCase())
        );
    }

    private Map<String, Price> fetchPricesForItemByNames(Collection<String> itemNames) {
        return priceRepository.findAllByIgnoreCaseCodeIn(itemNames).stream()
            .collect(Collectors.toMap(
                price -> price.getCode().toLowerCase(),
                price -> price)
            );
    }
    private BigDecimal calculateTotalPriceForItemGroup(List<LineItem> items, Price price) {
        int totalQuantity = items.stream().mapToInt(LineItem::quantity).sum();
        BigDecimal unitPrice = price.getPrice();
        if (price.getOffer() == null) {
            return unitPrice.multiply(BigDecimal.valueOf(totalQuantity));
        }

        PriceOffer offer = PriceOffer.parse(price.getOffer());
        if (totalQuantity < offer.quantity() ) {
            return unitPrice.multiply(BigDecimal.valueOf(totalQuantity));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        while (totalQuantity >= offer.quantity()) {
            totalPrice = totalPrice.add(offer.price());
            totalQuantity -= offer.quantity();
        }

        totalPrice = totalPrice.add(unitPrice.multiply(BigDecimal.valueOf(totalQuantity)));

        return totalPrice;
    }
}
