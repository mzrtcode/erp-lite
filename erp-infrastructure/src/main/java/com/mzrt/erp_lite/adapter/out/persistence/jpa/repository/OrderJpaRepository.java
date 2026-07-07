package com.mzrt.erp_lite.adapter.out.persistence.jpa.repository;

import com.mzrt.erp_lite.adapter.out.persistence.jpa.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {
}
