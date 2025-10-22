package com.example.point.intrastructure;

import com.example.point.domain.PointTransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionHistoryRepository extends JpaRepository<PointTransactionHistory, Long> {
    PointTransactionHistory findByRequestIdAndTransactionType(
            String requestId,
            PointTransactionHistory.TransactionType transactionType
    );
}
