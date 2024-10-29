package com.ioh.annotations;


import com.ioh.enums.OtpMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendOtpData implements Serializable {

    private String txnId;

    @Min(6)
    @Max(6)
    private String otp;

    @Min(10)
    @Max(10)

    private String mobileNumber;

    private String email;

    private OtpMode otpMode;

    private String type;
}