package com.mzrt.erp_lite.adapter.in.rest;

public record AddressDTO(
        String street,
        String suite,
        String city,
        String zipcode,
        GeoDTO geo
) {}
