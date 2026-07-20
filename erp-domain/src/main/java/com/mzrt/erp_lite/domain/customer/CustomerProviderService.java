package com.mzrt.erp_lite.domain.customer;

import java.util.Optional;

/*
 * Port for external service for JsonPlaceHolder
 */
public interface CustomerProviderService {
    Optional<CustomerInfo> findById(Long id);
    boolean existById(Long id);
}
