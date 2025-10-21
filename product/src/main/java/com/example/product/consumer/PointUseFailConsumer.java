package com.example.product.consumer;

import com.example.product.application.ProductService;
import com.example.product.application.dto.ProductBuyCancelCommand;
import com.example.product.consumer.dto.PointUseFailEvent;
import com.example.product.intrastructure.kafka.QuantityDecreasedFailProducer;
import com.example.product.intrastructure.kafka.dto.QuantityDecreasedFailEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PointUseFailConsumer {
    private final ProductService productService;
    private final QuantityDecreasedFailProducer quantityDecreasedFailProducer;

    public PointUseFailConsumer(ProductService productService, QuantityDecreasedFailProducer quantityDecreasedFailProducer) {
        this.productService = productService;
        this.quantityDecreasedFailProducer = quantityDecreasedFailProducer;
    }

    @KafkaListener(
            topics = "point-use-fail",
            groupId = "point-use-fail-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.product.consumer.dto.PointUseFailEvent"
            }
    )
    public void handle(PointUseFailEvent event) {
        productService.cancel(new ProductBuyCancelCommand(event.orderId().toString()));
        quantityDecreasedFailProducer.send(new QuantityDecreasedFailEvent(event.orderId()));
    }
}
