package com.example.point.application;

import com.example.point.application.dto.PointReserveCommand;
import com.example.point.application.dto.PointReserveConfirmCommand;
import com.example.point.domain.Point;
import com.example.point.domain.PointReservation;
import com.example.point.intrastructure.PointRepository;
import com.example.point.intrastructure.PointReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointReservationRepository pointReservationRepository;

    @Transactional
    public void tryReserve(PointReserveCommand command) {
        PointReservation reservation = pointReservationRepository.findByRequestId(command.requestId());

        if (reservation != null) {
            System.out.println("이미 예약된 요청입니다.");
            return;
        }

        Point point = pointRepository.findByUserId(command.userId());

        point.reserve(command.reserveAmount());
        pointReservationRepository.save(
                new PointReservation(
                        command.requestId(),
                        point.getId(),
                        command.reserveAmount()
                )
        );
    }

    @Transactional
    public void confirmReserve(PointReserveConfirmCommand command) {
        PointReservation reservation = pointReservationRepository.findByRequestId(command.requestId());

        if (reservation == null) {
            throw new RuntimeException("예약내역이 존재하지 않습니다.");
        }

        if (reservation.getStatus() == PointReservation.PointReservationStatus.CONFIRMED) {
            System.out.println("이미 확정된 예약입니다.");
            return;
        }

        Point point = pointRepository.findById(reservation.getPointId()).orElseThrow();

        point.confirm(reservation.getReservedAmount());
        reservation.confirm();

        pointRepository.save(point);
        pointReservationRepository.save(reservation);
    }
}
