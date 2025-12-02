package com.haiilo.checkout.core.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Valid
@Getter
@Setter
public class CheckoutRequest {

    @NotNull
    @NotEmpty
    private List<LineItem> items;
}
