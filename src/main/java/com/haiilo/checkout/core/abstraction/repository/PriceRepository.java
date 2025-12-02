package com.haiilo.checkout.core.abstraction.repository;

import com.haiilo.checkout.core.domain.entity.Price;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findAllByCodeIn(Collection<String> itemNames);
}
