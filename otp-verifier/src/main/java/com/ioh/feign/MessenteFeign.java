package com.ioh.feign;

import com.ioh.dto.MessenteRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "MessenteFeign-Number-Lookup", url = "${feignClient.messente.url}")
public interface MessenteFeign {

    @GetMapping("/number-lookup/")
    ResponseEntity<MessenteRespDTO> numberLookUp(@RequestParam String phone_number );
}
