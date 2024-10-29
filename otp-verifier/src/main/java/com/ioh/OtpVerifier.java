package com.ioh;

import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
@OpenAPI30
public class OtpVerifier {
    public static void main(String[] args) {
        SpringApplication.run(OtpVerifier.class, args);
    }

    @RequestMapping("/ping")
    public Message test() {
        return new Message("Pong from Otp Silent Verifier Service");
    }
    class Message {
        private String id = UUID.randomUUID().toString();
        private String content;
        Message() {
        }

        public Message(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }

}