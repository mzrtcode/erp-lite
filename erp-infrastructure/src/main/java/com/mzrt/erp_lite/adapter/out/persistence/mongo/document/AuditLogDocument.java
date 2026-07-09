package com.mzrt.erp_lite.adapter.out.persistence.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDocument {

    @Id
    private String id;

    private String className;

    private String methodName;

    private String userId;

    private Instant timestamp;

    private long executionTimeMs;

    private boolean success;

    private String errorMessage;

    private String ipAddress;

    private String endpoint;
}
