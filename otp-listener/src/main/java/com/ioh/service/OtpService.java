package com.ioh.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioh.dto.AuditStoreDto;
import com.ioh.dto.TransactionDTO;
import com.ioh.repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@Slf4j
public class OtpService {

    @Autowired
    SendOtpService sendOtpService;
    @Autowired
    OtpPublisher otpPublisher;
    @Autowired
    RedisRepository redisRepository;

    public Object generateOtp(String mobileNumber) {
        TransactionDTO transactionDTO= new TransactionDTO();
        int otp = (int) (Math.random() * 500000) + 100000;
        transactionDTO.setTxnId(UUID.randomUUID().toString());
        transactionDTO.setOtp(String.valueOf(otp));
        transactionDTO.setMobileNumber(mobileNumber);
        log.info("OtpService :: generateOtp :: sending otp {} to mobile Number {}",otp, mobileNumber);
        otpPublisher.sendOtpEvent(transactionDTO);
        log.info("OtpService :: generateOtp :: send otp {} successFully to mobile Number {}",otp,mobileNumber);
        transactionDTO.setMessage("OTP has been sent successfully to "+ transactionDTO.getMobileNumber().replaceAll(
                "\\d{6}(\\d{4})"
                ,
                "******$1"
        ));
        transactionDTO.setOtp(null);
       transactionDTO.setMobileNumber(null);
        AuditStoreDto auditStoreDto = new AuditStoreDto();
        auditStoreDto.setEntity("OTP");
        auditStoreDto.setEventType(AuditStoreDto.EventType.MOBILEOTP);
        auditStoreDto.setMessage("OTP SENT TO MOBILLE NUMBER "+mobileNumber);
        otpPublisher.sendAuditEvent(auditStoreDto);
        return transactionDTO;
    }

    public Object validateOTP(TransactionDTO transactionDTO) throws JsonProcessingException {
        String response=redisRepository.get(transactionDTO.getTxnId());
        if (response == null) {
            throw new IllegalArgumentException("Your OTP has expired.");
        }
        TransactionDTO transactionDTO1= new ObjectMapper().readValue(response, TransactionDTO.class);
        if (transactionDTO1.getOtp().equalsIgnoreCase(transactionDTO.getOtp())){
            transactionDTO1.setTxnId(null);
            transactionDTO1.setOtp(null);
            transactionDTO1.setMobileNumber(null);
            transactionDTO1.setMessage("Otp Verified SuccessFully");
            return transactionDTO1;
        }else{
            transactionDTO1.setTxnId(null);
            transactionDTO1.setOtp(null);
            transactionDTO1.setMobileNumber(null);
            transactionDTO1.setMessage("Incorrect Otp");
            return transactionDTO1;
        }
    }


}
