package com.ioh.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioh.dto.AuditStoreDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditPublisher {

    private static final ThreadLocal<ObjectMapper> OBJECT_MAPPER_THREAD_LOCAL = ThreadLocal.withInitial(ObjectMapper::new);

    @Value("${application.topic.auditEvent.queue}")
    private String auditEventTopic;


    @Autowired
    KafkaTemplate<String, String> stringEventKafkaTemplate;

    public void sendAuditEvent(AuditStoreDto auditStoreDto) {
        log.info("AuditPublisher:: sendAuditEvent:: Sending message to topic : " + auditEventTopic);
        try {
            String json = OBJECT_MAPPER_THREAD_LOCAL.get().writeValueAsString(auditStoreDto);
            stringEventKafkaTemplate.send(auditEventTopic, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
