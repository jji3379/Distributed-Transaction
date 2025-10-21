package com.example.product.consumer;

import com.example.product.application.ProductService;
import com.example.product.application.dto.ProductBuyCancelCommand;
import com.example.product.application.dto.ProductBuyCommand;
import com.example.product.application.dto.ProductBuyResult;
import com.example.product.consumer.dto.OrderPlacedEvent;
import com.example.product.intrastructure.kafka.QuantityDecreasedFailProducer;
import com.example.product.intrastructure.kafka.QuantityDecreasedProducer;
import com.example.product.intrastructure.kafka.dto.QuantityDecreasedEvent;
import com.example.product.intrastructure.kafka.dto.QuantityDecreasedFailEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedConsumer {
    private final ProductService productService;
    private final QuantityDecreasedProducer quantityDecreasedProducer;
    private final QuantityDecreasedFailProducer quantityDecreasedFailProducer;

    public OrderPlacedConsumer(ProductService productService, QuantityDecreasedProducer quantityDecreasedProducer, QuantityDecreasedFailProducer quantityDecreasedFailProducer) {
        this.productService = productService;
        this.quantityDecreasedProducer = quantityDecreasedProducer;
        this.quantityDecreasedFailProducer = quantityDecreasedFailProducer;
    }

    @KafkaListener(
            topics = "order-placed",
            groupId = "order-placed-consumer",
            properties = {
                    "spring.json.value.default.type=com.example.product.consumer.dto"
            }
    )
    public void handle(OrderPlacedEvent event) {
        String requestId = event.orderId().toString();

        try {
            ProductBuyResult result = productService.buy(
                    new ProductBuyCommand(
                            requestId,
                            event.productInfos()
                                    .stream()
                                    .map(item -> new ProductBuyCommand.ProductInfo(item.productId(), item.quantity()))
                                    .toList()
                    )
            );


            quantityDecreasedProducer.send(
                    new QuantityDecreasedEvent(
                            event.orderId(),
                            result.totalPrice()
                    )
            );
        } catch (Exception e) {
            productService.cancel(
                    new ProductBuyCancelCommand(requestId)
            );

            quantityDecreasedFailProducer.send(
                    new QuantityDecreasedFailEvent(event.orderId())
            );
        }
    }
}
