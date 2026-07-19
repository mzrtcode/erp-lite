package com.mzrt.erp_lite.adapter.out.persistence.jsonplaceholder.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "jsonplaceholder.api")
public record JsonPlaceholderConfigModel(

        @NotBlank(message = "jsonplaceholder.api.base-url no puede estar vacío")
        String baseUrl,

        @NotBlank(message = "jsonplaceholder.api.users-endpoint no puede estar vacío")
        String usersEndpoint,

        @Positive(message = "jsonplaceholder.api.connect-timeout debe ser mayor a 0")
        int connectTimeout,

        @Positive(message = "jsonplaceholder.api.read-timeout debe ser mayor a 0")
        int readTimeout,

        boolean enabled
) {}
