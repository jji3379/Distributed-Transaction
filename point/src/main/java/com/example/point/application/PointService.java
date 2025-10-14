package com.example.point.application;

import com.example.point.application.dto.PointUseCommand;
import com.example.point.domain.Point;
import com.example.point.domain.PointTransactionHistory;
import com.example.point.intrastructure.PointRepository;
import com.example.point.intrastructure.PointTransactionHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final PointRepository pointRepository;
    private final PointTransactionHistoryRepository pointTransactionHistoryRepository;

    public PointService(PointRepository pointRepository, PointTransactionHistoryRepository pointTransactionHistoryRepository) {
        this.pointRepository = pointRepository;
        this.pointTransactionHistoryRepository = pointTransactionHistoryRepository;
    }

    @Transactional
    public void use(PointUseCommand command) {
        PointTransactionHistory useHistory = pointTransactionHistoryRepository.findByRequestIdAndTransactionType(
                command.requestId(),
                PointTransactionHistory.TransactionType.USE
        );

        if (useHistory != null) {
            System.out.println("이미 사용한 이력이 존재합니다.");
            return;
        }

        Point point = pointRepository.findByUserId(command.userId())
                .orElseThrow(() -> new RuntimeException("포인트가 존재하지 않습니다."));

        point.use(command.amount());
        pointTransactionHistoryRepository.save(
                new PointTransactionHistory(
                        command.requestId(),
                        point.getId(),
                        command.amount(),
                        PointTransactionHistory.TransactionType.USE
                )
        );
    }
}
