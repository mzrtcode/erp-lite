package com.mzrt.erp_lite.adapter.in.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mzrt.erp_lite.adapter.in.rest.UserDTO;
import com.mzrt.erp_lite.domain.customer.CustomerInfo;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "address.street", target = "address")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.zipcode", target = "zipcode")
    @Mapping(source = "company.name", target = "companyName")
    CustomerInfo toCustomerInfo(UserDTO userDTO);
}
