package com.example.monolithic.order.application;

import com.example.monolithic.order.application.dto.CreateOrderCommand;
import com.example.monolithic.order.application.dto.PlaceOrderCommand;
import com.example.monolithic.order.domain.Order;
import com.example.monolithic.order.domain.OrderItem;
import com.example.monolithic.order.infrastructure.OrderItemRepository;
import com.example.monolithic.order.infrastructure.OrderRepository;
import com.example.monolithic.point.application.PointService;
import com.example.monolithic.product.application.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PointService pointService;
    private final ProductService productService;

    @Transactional
    public void createOrder(CreateOrderCommand command) {
        Order order = orderRepository.save(new Order());
        List<OrderItem> orderItems = command.orderItems()
                .stream()
                .map(item -> new OrderItem(order.getId(), item.productId(), item.quantity()))
                .toList();

        orderItemRepository.saveAll(orderItems);
    }

    @Transactional
    public void placeOrder(PlaceOrderCommand placeOrderCommand) {
        Order order = orderRepository.save(new Order());
        Long totalPrice = 0L;

        for (PlaceOrderCommand.OrderItem item : placeOrderCommand.orderItems()) {
            OrderItem orderItem = new OrderItem(order.getId(), item.productId(), item.quantity());
            orderItemRepository.save(orderItem);

            Long price = productService.buy(item.productId(), item.quantity());
            totalPrice += price;
        }

        pointService.use(1L, totalPrice);
    }
}
