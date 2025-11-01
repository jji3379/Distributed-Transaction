package com.example.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
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

    public void request() {
        if(status != OrderStatus.CREATED) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        status = OrderStatus.REQUESTED;
    }

    public void fail() {
        if(status != OrderStatus.REQUESTED) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        status = OrderStatus.FAILED;
    }

    public enum OrderStatus {
        CREATED, COMPLETED, REQUESTED, FAILED
    }

    public Long getId() {
        return id;
    }
}
