package com.mzrt.erp_lite.adapter.out.persistence.jpa.repository;

import com.mzrt.erp_lite.adapter.out.persistence.jpa.entity.OrderProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderProductJpaRepository extends JpaRepository<OrderProductJpaEntity, UUID> {
}
