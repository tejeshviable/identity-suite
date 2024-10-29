package identity_suite_management.controller;

import identity_suite_management.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ServiceProviderController {
    @Autowired
    private ServiceProviderService serviceProviderService;


    @PostMapping("/getProviderName")
    public ResponseEntity<String> getServiceProviderName(@RequestParam String mobileNumber){
        String serviceProviderName = serviceProviderService.getServiceProviderName(mobileNumber);
        return ResponseEntity.ok(serviceProviderName);
    }
}
