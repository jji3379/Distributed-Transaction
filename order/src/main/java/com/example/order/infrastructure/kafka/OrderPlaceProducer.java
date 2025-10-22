package com.example.order.infrastructure.kafka;

import com.example.order.infrastructure.kafka.dto.OrderPlacedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderPlaceProducer {
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderPlaceProducer(KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderPlacedEvent event) {
        kafkaTemplate.send(
                "order-placed",
                event.orderId().toString(),
                event
        );
    }
}
