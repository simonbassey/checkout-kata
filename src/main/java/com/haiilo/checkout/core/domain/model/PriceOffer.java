package com.haiilo.checkout.core.domain.model;

import com.haiilo.checkout.core.domain.exception.InvalidPriceOfferFormatException;
import java.math.BigDecimal;
import java.util.regex.Matcher;

public record PriceOffer(int quantity, BigDecimal price) {

    public static PriceOffer parse(String offerText) {
        Matcher matcher = Constants.VALID_PRICE_OFFER.matcher(offerText);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new InvalidPriceOfferFormatException(offerText);
        }
        int quantity = Integer.parseInt(matcher.group(1));
        BigDecimal price = new BigDecimal(matcher.group(2));
        return new PriceOffer(quantity, price);
    }
}
