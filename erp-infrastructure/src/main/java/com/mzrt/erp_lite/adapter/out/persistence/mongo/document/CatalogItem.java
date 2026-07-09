package com.mzrt.erp_lite.adapter.out.persistence.mongo.document;

import java.util.Map;

public record CatalogItem(
    String id,
    String code,
    String value,
    String description,
    int displayOrder,
    Map<String, Object> metadata
) {}
