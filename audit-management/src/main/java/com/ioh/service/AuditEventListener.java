package com.ioh.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.ioh.dto.AuditStoreDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuditEventListener {

    @Autowired
    private AuditService auditService;


    @KafkaListener(topics = "${application.topic.audit.queue}",
            groupId = "${application.topic.audit.group-id}",
            containerFactory = "auditEventConcurrentKafkaListenerContainerFactory")
    public void listenAuditEventTopic(String auditEventString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            log.info("This is the data we are getting inside consumer :: {}",auditEventString);
            AuditStoreDto auditStoreDto = mapper.readValue(auditEventString, AuditStoreDto.class);
            auditService.registerAudit(auditStoreDto);
        } catch (Exception e) {
            log.error("Error processing event : " + auditEventString, e.getMessage(), e);
        }
    }
}
