package com.example.order.domain;

import jakarta.persistence.*;
import lombok.Getter;

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

    public void reserve() {
        if (this.status != OrderStatus.CREATED) {
            throw new RuntimeException("생성된 단계에서만 예약할 수 있습니다.");
        }

        this.status = OrderStatus.RESERVED;
    }

    public void cancel() {
        if (this.status != OrderStatus.RESERVED) {
            throw new RuntimeException("예약단계에서만 취소할 수 있습니다.");
        }

        this.status = OrderStatus.CANCELLED;
    }

    public void confirm() {
        if (this.status != OrderStatus.RESERVED && this.status != OrderStatus.PENDING) {
            throw new RuntimeException("예약단계 혹은 Pending 에서만 확정할 수 있습니다.");
        }

        this.status = OrderStatus.CONFIRMED;
    }

    public void pending() {
        if (this.status != OrderStatus.RESERVED) {
            throw new RuntimeException("예약단계에서만 확정할 수 있습니다.");
        }

        this.status = OrderStatus.PENDING;
    }

    public enum OrderStatus {
        CREATED, COMPLETED, RESERVED, CANCELLED, CONFIRMED, PENDING,
    }

    public Long getId() {
        return id;
    }
}
