package com.ioh.service;


import com.ioh.dto.TransactionDTO;
import com.ioh.repository.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@Slf4j
public class OTPKafkaListener {


    @Autowired
    SendOtpService sendOtpService;

    @Autowired
    RedisRepository redisRepository;

    @Autowired
    Gson gson;

    @Value("${redis.send.otp.data.validity}")
    int validity;
        @KafkaListener(topics = "${application.topic.otpEvent.queue}",
        groupId = "${application.topic.otpEvent.queue}",
        containerFactory = "otpConcurrentKafkaListenerContainerFactory")
    public void listenVehiclePacketTopic(String message) throws JsonProcessingException {
        try {
            log.info("OTPKafkaListener:: listenVehiclePacketTopic:: mobile number received - " + message);
            ObjectMapper mapper = new ObjectMapper();
            TransactionDTO transactionDTO = mapper.readValue(message, TransactionDTO.class);
            redisRepository.save(String.valueOf(transactionDTO.getTxnId()), gson.toJson(transactionDTO), Date.from(Instant.now().plus(validity, MINUTES)));
            log.info("OTPKafkaListener:: listenVehiclePacketTopic:: data saved in redis");
            sendOtpService.sendOtp(transactionDTO.getMobileNumber(),transactionDTO.getOtp());
        }
        catch (Exception e){
            log.info("OTPKafkaListener:: listenVehiclePacketTopic::Error :: {}", e.getMessage());
        }
    }
}
