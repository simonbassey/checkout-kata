package com.haiilo.checkout.core.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prices")
@Getter
@Setter
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "item_code", nullable = false, unique = true)
    private String code;

    @NotNull
    @DecimalMin(value = "0.1", message = "Price must be at least 0.1")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String offer;
}
