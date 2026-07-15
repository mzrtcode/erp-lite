package com.mzrt.erp_lite.domain.repository;

import com.mzrt.erp_lite.domain.catalog.Catalog;
import com.mzrt.erp_lite.domain.catalog.CatalogItem;
import com.mzrt.erp_lite.domain.catalog.CatalogType;

import java.util.List;
import java.util.Optional;

/*
 * Read-Only port for Catalog
 */
public interface CatalogRepository {

    Optional<Catalog> findByType(CatalogType type);
    List<CatalogItem> findItemByType(CatalogType type);
    Optional<CatalogItem> findItemByTypeAndCode(CatalogType type, String code);

}
