package com.ioh.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ioh.dto.TransactionDTO;
import com.ioh.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ioh.enums.Specification.*;

@RestController
@RequestMapping
public class OtpController {
    @Autowired
    OtpService otpService;
    @PostMapping(VALIDATE_MOBILE)
    public ResponseEntity<?> generateOtp(@PathVariable String mobileNumber ){
        return ResponseEntity.ok(otpService.generateOtp(mobileNumber));
    }

    @PostMapping(VALIDATE_OTP)
    public ResponseEntity<?>validateOtp( @RequestBody TransactionDTO transactionDTO) throws JsonProcessingException {
        return ResponseEntity.ok(otpService.validateOTP(transactionDTO));
    }

}
