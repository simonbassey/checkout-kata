package com.haiilo.checkout.core.domain.entity;

import com.haiilo.checkout.core.domain.exception.InvalidPriceOfferFormatException;
import com.haiilo.checkout.core.domain.model.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prices", indexes = {
    @Index(name = "IX_price_item_code", columnList = "item_code", unique = true)
})
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

    @PrePersist
    @PreUpdate
    private void normalizeItemCodeAndValidatePriceOffer() {
        if (code != null) {
            code = code.trim().toLowerCase();
        }
        if (offer != null && !offer.isBlank() && !Constants.VALID_PRICE_OFFER.matcher(offer).matches()) {
            throw new InvalidPriceOfferFormatException(offer);
        }
    }
}
