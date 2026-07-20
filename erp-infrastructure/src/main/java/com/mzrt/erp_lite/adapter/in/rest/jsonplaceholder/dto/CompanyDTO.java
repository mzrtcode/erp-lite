package com.mzrt.erp_lite.adapter.in.rest.jsonplaceholder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompanyDTO(
        String name,
        @JsonProperty("catchPhrase")
        String cp,
        String bs
) {}
