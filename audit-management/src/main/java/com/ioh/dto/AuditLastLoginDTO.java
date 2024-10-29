package com.ioh.dto;

import lombok.Data;

@Data
public class AuditLastLoginDTO {
    private String lastLoginInDays;
    private String lastLoginInTimes;
}
