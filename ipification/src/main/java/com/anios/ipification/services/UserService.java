package com.anios.ipification.services;


import com.anios.ipification.feign.IpificationFeign;
import com.anios.ipification.requestDTO.RedisDto;
import com.anios.ipification.requestDTO.MobileRequestDTO;
import com.anios.ipification.responseDTO.GenerateUrlResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Value("${url.redirect}")
    String url;
    @Value("${url.callback}")
    String url1;

    @Value("${url.clientId}")
    String clientId;

    @Value("${url.client-callback-uri}")
    String clientCallbackUri;

    @Value("${url.clientSecret}")
    String clientSecret;
    @Autowired
    RedisService redisService;

    @Autowired
    IpificationFeign ipificationFeign;


    public GenerateUrlResponseDTO generateUrl(MobileRequestDTO mobileRequestDTO)  {

        GenerateUrlResponseDTO generateUrlResponseDTO = new GenerateUrlResponseDTO();

        String requestId = UUID.randomUUID().toString();
        String newUrl = url1+clientCallbackUri+"&client_id="+clientId+"&scope=openid ip:phone_verify&state="+requestId+"&login_hint="+ mobileRequestDTO.getMobileNumber();
        generateUrlResponseDTO.setRequestId(requestId);
        generateUrlResponseDTO.setRedirectionUrl(newUrl);
        RedisDto redisDto = new RedisDto();
        redisDto.setRequestId(generateUrlResponseDTO.getRequestId());
        redisService.saveDataToRedis(mobileRequestDTO.getMobileNumber(),redisDto);

        return generateUrlResponseDTO;
    }

    public ResponseEntity<?> saveVerificationStatus(MultiValueMap<String,String> values) {

        values.add("grant_type","authorization_code");
        values.add("redirect_uri",clientCallbackUri);
        values.add("client_id",clientId);
        values.add("client_secret",clientSecret);

        ResponseEntity<?> tokens =  ipificationFeign.fetchToken(values);

        if(tokens.getStatusCode().is2xxSuccessful()){
            Map<String, Object> body = (Map<String, Object>) tokens.getBody();

            if (body != null && body.containsKey("access_token")) {
                String accessToken = (String) body.get("access_token");
                String bearerToken = "Bearer "+accessToken;

                ResponseEntity<?> userInfoResponse = ipificationFeign.userDetails(bearerToken);
                if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> userBody = (Map<String,Object>) userInfoResponse.getBody();
                    if(userBody != null && userBody.containsKey("phone_number_verified")){
                        String status = (String) userBody.get("phone_number_verified");
                        String login_hint = (String) userBody.get("login_hint");


                        RedisDto redisDto = (RedisDto) redisService.getDataFromRedis(login_hint);
                        redisDto.setStatus(status);

                        redisService.saveDataToRedis(login_hint,redisDto);

                        log.info("redis-data : {}", redisService.getDataFromRedis(login_hint));
                    }
                    return userInfoResponse;
                } else {
                    return ResponseEntity.status(userInfoResponse.getStatusCode())
                            .body("Failed to retrieve user details");
                }
            }
        }
        return tokens;
    }

    public ResponseEntity<?> saveVerificationStatus(String code) {

        MultiValueMap<String,String> values = new LinkedMultiValueMap<>();
        values.add("code",code);
        values.add("grant_type","authorization_code");
        values.add("redirect_uri",clientCallbackUri);
        values.add("client_id",clientId);
        values.add("client_secret",clientSecret);

        ResponseEntity<?> tokens =  ipificationFeign.fetchToken(values);

        if(tokens.getStatusCode().is2xxSuccessful()){
            Map<String, Object> body = (Map<String, Object>) tokens.getBody();

            if (body != null && body.containsKey("access_token")) {
                String accessToken = (String) body.get("access_token");
                String bearerToken = "Bearer "+accessToken;

                ResponseEntity<?> userInfoResponse = ipificationFeign.userDetails(bearerToken);
                if (userInfoResponse.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> userBody = (Map<String,Object>) userInfoResponse.getBody();
                    if(userBody != null && userBody.containsKey("phone_number_verified")){
                        String status = (String) userBody.get("phone_number_verified");
                        String login_hint = (String) userBody.get("login_hint");


                        RedisDto redisDto = (RedisDto) redisService.getDataFromRedis(login_hint);
                        redisDto.setStatus(status);

                        redisService.saveDataToRedis(login_hint,redisDto);

                        log.info("redis-data : {}", redisService.getDataFromRedis(login_hint));
                    }
                    return userInfoResponse;
                } else {
                    return ResponseEntity.status(userInfoResponse.getStatusCode())
                            .body("Failed to retrieve user details");
                }
            }
        }
        return tokens;
    }

    public RedisDto getUserStatus(String mobileNumber) {
        RedisDto redisDto;
        if(redisService.getDataFromRedis(mobileNumber) == null)
        {
            redisDto = new RedisDto();
            redisDto.setStatus("false");
            return redisDto;
        }
        return (RedisDto) redisService.getDataFromRedis(mobileNumber);

    }


}
