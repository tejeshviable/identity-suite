package com.ioh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditStoreDto {
    private Date createdDate;
    private String auditCategory;
    private Map<String, String> metaMap = new HashMap<>();
    private String description;
    private String message;
    private String entity;
    private EventType eventType = EventType.UPDATE;

    private String createdBy;
    private Long updatedDate;
    private String updatedBy;



    public enum EventType {
        MOBILEOTP, UPDATE;
    }
}
