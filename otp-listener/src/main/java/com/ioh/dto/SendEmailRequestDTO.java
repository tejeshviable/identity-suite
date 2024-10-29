package com.ioh.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendEmailRequestDTO {


    @Schema(name = "txnId", description = "Transaction ID")
    private String txnId;

    private String emailId;
    private String URL;
    private String mailContent;
    private String otp;
    private String type;
    private String subject;
    private String name;
    private String facilityId;
    private String contactName;
}
