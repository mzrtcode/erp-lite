package com.mzrt.erp_lite.adapter.out.persistence.mongo.repository;

import com.mzrt.erp_lite.adapter.out.persistence.mongo.document.AuditLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
}
