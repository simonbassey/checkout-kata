package com.haiilo.checkout.core.domain.exception;

public class ItemPriceNotFoundException extends RuntimeException {

    public ItemPriceNotFoundException(String itemName) {
        super("No price found for item with name %s".formatted(itemName));
    }
}
