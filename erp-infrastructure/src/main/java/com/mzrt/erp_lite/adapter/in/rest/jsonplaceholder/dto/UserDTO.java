package com.mzrt.erp_lite.adapter.in.rest.jsonplaceholder.dto;

public record UserDTO(
        Long id,
        String name,
        String username,
        String email,
        AddressDTO address,
        String phone,
        String website,
        CompanyDTO company
) { }
