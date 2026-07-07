package com.mzrt.erp_lite.adapter.out.persistence.mongo.repository;

import com.mzrt.erp_lite.adapter.out.persistence.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDocumentMongoRepository extends MongoRepository<ProductDocument, String> {
}
