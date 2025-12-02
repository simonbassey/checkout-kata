package com.haiilo.checkout.core.domain.exception;

public class InvalidPriceOfferFormatException extends RuntimeException {

    public InvalidPriceOfferFormatException(String offerText) {
        super("Invalid price offer format: " + offerText);
    }
}
