package com.mzrt.erp_lite.adapter.out.persistence.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "product_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {

    @Id
    private String id;

    private String sku;

    private String name;

    private String description;

    private BigDecimal price;

    private String currency;

    private int stock;

    private String categoryId;

    private String categoryName;

    private String imageUrl;

    private boolean active;

    private List<String> tags;

    private ProductSpecifications specifications;

    private Instant createdAt;

    private Instant updatedAt;
}
