package com.example.product.intrastructure.kafka.dto;

public record QuantityDecreasedEvent(Long orderId, Long totalPrice) {
}
