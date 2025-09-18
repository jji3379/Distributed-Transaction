package com.example.order.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
        this.status = OrderStatus.CREATED;
    }

    public void complete() {
        status = OrderStatus.COMPLETED;
    }

    public enum OrderStatus {
        CREATED, COMPLETED
    }
}
