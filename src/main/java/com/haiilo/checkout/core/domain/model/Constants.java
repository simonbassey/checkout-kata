package com.haiilo.checkout.core.domain.model;

import java.util.regex.Pattern;

public class Constants {
    public static final Pattern VALID_PRICE_OFFER = Pattern.compile("(\\d+) for (\\d+(?:\\.\\d{1,2})?)");
}
