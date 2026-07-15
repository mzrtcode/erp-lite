package com.mzrt.erp_lite.domain.customer;

/*
 * Value Object immutable for JSONPlaceHolder
 */
public record CustomerInfo(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String city,
        String zipcode,
        String companyName
) {
    public CustomerInfo {

        if (id == null){
            throw new IllegalArgumentException("Id is null");
        }

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name is not present");
        }

    }
}
