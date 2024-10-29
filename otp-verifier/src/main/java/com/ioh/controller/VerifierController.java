package com.ioh.controller;

import com.ioh.dto.SilentAuthRequestDTO;
import com.ioh.enums.Specification;
import com.ioh.service.VerifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ioh.enums.Specification.*;

@RestController
@RequestMapping("/validate")
public class VerifierController {



@Autowired
    private VerifierService verifierService;
    @PostMapping(VALIDATE_COUNTRY_AND_MOBILE)
    public ResponseEntity<?> validateMobileNo(@PathVariable String countryCode, @PathVariable String mobileNumber ){
        return ResponseEntity.ok(verifierService.validateMobileNo(countryCode,mobileNumber));
    }


    @PostMapping(VALIDATE_SILENT_VERIFICATION)
    public ResponseEntity<?> SilentValidateMobileNo(@RequestHeader String Authorization, @RequestBody SilentAuthRequestDTO silentAuthRequestDTO ){
        return ResponseEntity.ok(verifierService.SilentValidateMobileNo(Authorization,silentAuthRequestDTO));
    }

}
