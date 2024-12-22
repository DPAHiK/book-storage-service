package com.example.book_storage_service.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private ProducerService producerService;

    @BeforeEach
    void setUp() {
        producerService = new ProducerService(kafkaTemplate);
    }

    @Test
    void testSendBookId() {
        producerService.sendBookId("test-topic", "12345");

        verify(kafkaTemplate, times(1)).send("test-topic", "12345");
    }
}
