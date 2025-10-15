package com.example.order.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Table(name = "compensation_registries")
@Entity
@NoArgsConstructor
public class CompensationRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private CompensationRegistryStatus status;

    public CompensationRegistry(Long orderId) {
        this.orderId = orderId;
        this.status = CompensationRegistryStatus.PENDING;
    }

    public enum CompensationRegistryStatus {
        PENDING, COMPLETE
    }
}
