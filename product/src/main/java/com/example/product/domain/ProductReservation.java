package com.example.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_reservations")
@NoArgsConstructor
@Getter
public class ProductReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private Long productId;

    private Long reservedQuantity;

    private Long reservedPrice;

    @Enumerated(EnumType.STRING)

    private ProductReservationStatus status;

    public ProductReservation(String requestId, Long productId, Long reservedQuantity, Long reservedPrice) {
        this.requestId = requestId;
        this.productId = productId;
        this.reservedQuantity = reservedQuantity;
        this.reservedPrice = reservedPrice;
        this.status = ProductReservationStatus.RESERVED;
    }

    public void confirm() {
        if (this.status == ProductReservationStatus.CANCELLED) {
            throw new RuntimeException("이미 취소된 예약입니다.");
        }

        this.status = ProductReservationStatus.CONFIRMED;
    }

    public enum ProductReservationStatus {
        RESERVED,
        CONFIRMED,
        CANCELLED,
        PENDING
    }
}
