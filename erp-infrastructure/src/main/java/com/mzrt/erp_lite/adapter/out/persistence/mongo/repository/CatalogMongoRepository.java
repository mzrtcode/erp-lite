package com.mzrt.erp_lite.adapter.out.persistence.mongo.repository;

import com.mzrt.erp_lite.adapter.out.persistence.mongo.document.CatalogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogMongoRepository extends MongoRepository<CatalogDocument, String> {
}
