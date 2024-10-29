package com.ioh.config;



import com.ioh.service.SendOtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class KafkaConsumerConfig {

    @Autowired
    private SendOtpService sendOtpService;


    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${application.topic.sendVerifyOTP.concurrency}")
    private Integer concurrency;

    public ConsumerFactory<String, String> otpConsumerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new StringDeserializer());

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> otpConcurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(otpConsumerFactory());
        factory.setConcurrency(concurrency);
        return factory;
    }


}
