package com.anios.ipification.controller;

import com.anios.ipification.requestDTO.MobileRequestDTO;
import com.anios.ipification.requestDTO.RedisDto;
import com.anios.ipification.responseDTO.GenerateUrlResponseDTO;
import com.anios.ipification.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create/callback")
    public ResponseEntity<GenerateUrlResponseDTO> generateUrl(@RequestBody MobileRequestDTO mobileRequestDTO){
        return new ResponseEntity<>(userService.generateUrl(mobileRequestDTO), HttpStatus.MOVED_TEMPORARILY);
    }

    @PostMapping(value="/status/save")
    public ResponseEntity<?> saveVerificationStatus(@RequestBody MultiValueMap<String, String> formData)
    {
        return new ResponseEntity<>(userService.saveVerificationStatus(formData).getBody(), HttpStatus.OK);
    }

    @GetMapping(value = "/user/status")
    public ResponseEntity<RedisDto> getUserStatus(@RequestBody MobileRequestDTO mobileRequestDTO){
        return new ResponseEntity<>(userService.getUserStatus(mobileRequestDTO.getMobileNumber()),HttpStatus.OK);
    }



}