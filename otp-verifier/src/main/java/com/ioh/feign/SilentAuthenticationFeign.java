package com.ioh.feign;


import com.ioh.dto.*;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "Silent-Authentication", url = "${feignClient.silentAuthentication.url}")
public interface SilentAuthenticationFeign {
    @PostMapping()
    ResponseEntity<SilentAuthResponseDTO> sendVerificationNumber(@RequestHeader String Authorization, @RequestBody SilentAuthRequestDTO silentAuthRequestDTO);

    @GetMapping("/{url}/silent-auth/redirect")
    UrlResponseDTO generateCode(@PathVariable String url);
    @PostMapping("/{requestId}")
    ResponseEntity<CodeVerifyResponseDTO> verifyCode (@RequestHeader String Authorization, @PathVariable String requestId, @RequestBody CodeDTO code);

}
