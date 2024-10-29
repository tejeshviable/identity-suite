package com.ioh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(value = "Send-Otp", url = "${feignClient.sendOtp.url}")
public interface SendOtpFeign {
    @PostMapping("/{mobileNumber}")
    ResponseEntity<?> generateOtp(@PathVariable String mobileNumber );
}
