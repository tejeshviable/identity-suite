package com.ioh.controller;


import com.ioh.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuditController {

    @Autowired
    AuditService auditService;

    @GetMapping("/getData")
    public ResponseEntity<?>getAudit(@RequestParam String HprId){
        return ResponseEntity.ok(auditService.getAuditDetails(HprId));
    }
}
