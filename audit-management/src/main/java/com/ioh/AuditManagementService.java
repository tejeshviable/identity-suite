package com.ioh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@SpringBootApplication
public class AuditManagementService {
    public static void main(String[] args) {
        SpringApplication.run(AuditManagementService.class, args);
    }

    @RequestMapping("/ping")
    public Message test() {
        return new Message("Pong from Audit Management");
    }

    class Message {
        private String id = UUID.randomUUID().toString();
        private String content;

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