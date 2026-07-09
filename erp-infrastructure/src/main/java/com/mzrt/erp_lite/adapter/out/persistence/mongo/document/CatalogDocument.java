package com.mzrt.erp_lite.adapter.out.persistence.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "catalogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogDocument {

    @Id
    private String id;

    private CatalogType catalogType;

    private String name;

    private String description;

    private boolean active;

    private List<CatalogItem> items;

    private Instant createdAt;

    private Instant updatedAt;
}
