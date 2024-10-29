package com.ioh.dto;

import lombok.Data;

@Data
public class MessenteRespDTO {

    private String formattedPhoneNumber;
    private String countryName;
    private String originalCarrierName;
    private String timeZone;
}
