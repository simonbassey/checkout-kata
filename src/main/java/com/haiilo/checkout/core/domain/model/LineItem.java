package com.haiilo.checkout.core.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LineItem (
    @NotNull String itemName,
    @Min(1) int quantity
) {}
