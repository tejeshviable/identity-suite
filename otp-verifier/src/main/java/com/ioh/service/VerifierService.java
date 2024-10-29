package com.ioh.service;

import com.ioh.dto.*;
import com.ioh.feign.MessenteFeign;
import com.ioh.feign.SendOtpFeign;
import com.ioh.feign.SilentAuthenticationFeign;
import com.ioh.utility.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VerifierService {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    SendOtpFeign sendOtpFeign;
    @Autowired
    AuditPublisher auditPublisher;
    @Autowired
    SilentAuthenticationFeign silentAuthenticationFeign;

    @Autowired
    MessenteFeign messenteFeign;

    public Object validateMobileNo(String countryCode,String mobileNumber) {
        String token= jwtTokenUtil.generateToken();
        log.info("VerifierService::validateMobileNo:: tokenGenerateSuccessFully");
        try {
            SilentAuthRequestDTO silentAuthRequestDTO = new SilentAuthRequestDTO();
            silentAuthRequestDTO.setBrand("Your Brand");
            List<SilentAuthRequestDTO.Workflow> workFlows = new ArrayList<>();
            SilentAuthRequestDTO.Workflow workFlow = new SilentAuthRequestDTO.Workflow();
            workFlow.setTo(countryCode+mobileNumber);
            workFlow.setChannel("silent_auth");
            workFlow.setSandbox(true);
            workFlows.add(workFlow);
            silentAuthRequestDTO.setWorkflow(workFlows);
            log.info("VerifierService::validateMobileNo:: request::{}",silentAuthRequestDTO);
            ResponseEntity<SilentAuthResponseDTO> silentAuthResponseDTO = silentAuthenticationFeign.sendVerificationNumber("Bearer " + token, silentAuthRequestDTO);
            log.info("VerifierService::validateMobileNo:: response::{}",silentAuthResponseDTO);
            String requestId = silentAuthResponseDTO.getBody().getRequest_id();
            UrlResponseDTO urlResponseDTO = getUrl(requestId);
            CodeDTO dto = new CodeDTO();
            dto.setCode(urlResponseDTO.getCode());
            log.info("VerifierService::validateMobileNo:: request::{}",dto);
            ResponseEntity<CodeVerifyResponseDTO> codeVerifyResponseDTO = silentAuthenticationFeign.verifyCode("Bearer " + token, urlResponseDTO.getRequest_id(), dto);
            log.info("VerifierService::validateMobileNo:: response::{}",codeVerifyResponseDTO);
            VerifiedRespDTO verifiedRespDTO= new VerifiedRespDTO();
            verifiedRespDTO.setMessage("Mobile Number Verified Successfully");
            AuditStoreDto auditStoreDto = new AuditStoreDto();
            auditStoreDto.setEntity("VALIDATE_MOBILE_NUMBER");
            auditStoreDto.setEventType(AuditStoreDto.EventType.MOBILEOTP);
            auditStoreDto.setMessage(" MOBILE NUMBER VALIDATED SUCCESSFULLY "+mobileNumber);
            auditPublisher.sendAuditEvent(auditStoreDto);
            return verifiedRespDTO;
        }catch (Exception e){
           return sendOtpFeign.generateOtp(mobileNumber).getBody();
        }

    }

    public UrlResponseDTO getUrl(String url){
        log.info("VerifierService::getUrl:: request::{}",url);
        UrlResponseDTO urlResponseDTO= silentAuthenticationFeign.generateCode(url);
        log.info("VerifierService::getUrl:: response::{}",urlResponseDTO);
        return  urlResponseDTO;
    }

    public Object SilentValidateMobileNo(String authorization, SilentAuthRequestDTO silentAuthRequestDTO) {
        if (silentAuthRequestDTO != null && !silentAuthRequestDTO.getWorkflow().isEmpty()) {
            for (int i = 0; i < silentAuthRequestDTO.getWorkflow().size(); i++) {
                ResponseEntity<MessenteRespDTO> messenteRespDTO= messenteFeign.numberLookUp(silentAuthRequestDTO.getWorkflow().get(i).getTo());
                log.info("messenteRespDTO:{}",messenteRespDTO.getBody().getOriginalCarrierName());
                return sendOtpFeign.generateOtp(silentAuthRequestDTO.getWorkflow().get(i).getTo()).getBody();
            }
        }
        return null;
    }

}
