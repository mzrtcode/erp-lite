package com.mzrt.erp_lite.adapter.out.persistence.mongo.document;

public record ProductSpecifications(
    String processor,
    String ram,
    String storage,
    String display,
    String weight
) {}
