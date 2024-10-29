package com.ioh.service;


import com.ioh.dto.AuditLastLoginDTO;
import com.ioh.dto.AuditStoreDto;
import com.ioh.repository.AuditStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class AuditService {
    @Autowired
    AuditStoreRepository auditStoreRepository;

    public void registerAudit(AuditStoreDto auditDTO){
        auditStoreRepository.save(auditDTO);
    }

    public Object getAuditDetails(String HprId) {
        List<String> message = new ArrayList<>(Arrays.asList(
                "Login Via User Detail!",
                "Login Confirm With Aadhaar OTP!",
                "Login Confirm With Mobile OTP!"
        ));
        Map<String, String> metaMap = new HashMap<>();
        metaMap.put("HprId", HprId);
        List<AuditStoreDto> auditStoreDto = auditStoreRepository.findByMessageInOrderByMessageAsc(message);
        AuditLastLoginDTO auditLastLoginDTO = new AuditLastLoginDTO();
        for(int i= auditStoreDto.size()-1; i>0 ; i--){
            if(auditStoreDto.get(i).getMetaMap().get("HprId").equalsIgnoreCase(HprId) && auditStoreDto.get(i).getMetaMap().get("time")!=null){

                String lastLoginDay = convertEpochToDateTime(Long.parseLong(auditStoreDto.get(i).getMetaMap().get("time")));

                long difference = calculateDateDifference(lastLoginDay);

                auditLastLoginDTO.setLastLoginInDays(String.valueOf(difference));
                auditLastLoginDTO.setLastLoginInTimes(lastLoginDay);

                return auditLastLoginDTO;
            }

        }
        return "No Data Found";
    }



    public static String convertEpochToDateTime(long epochTimestamp) {
        Instant instant = Instant.ofEpochMilli(epochTimestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return localDateTime.format(formatter);
    }

    private long calculateDateDifference(String lastLoginDay) {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastLoginDate = LocalDate.parse(lastLoginDay, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return ChronoUnit.DAYS.between(lastLoginDate, currentDate);
    }

}
