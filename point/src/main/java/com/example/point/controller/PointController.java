package com.example.point.controller;

import com.example.point.application.PointFacadeService;
import com.example.point.application.RedisLockService;
import com.example.point.controller.dto.PointReserveCancelRequest;
import com.example.point.controller.dto.PointReserveConfirmRequest;
import com.example.point.controller.dto.PointReserveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PointController {
    private final PointFacadeService pointFacadeService;
    private final RedisLockService redisLockService;

    @PostMapping("/point/reserve")
    public void reserve(@RequestBody PointReserveRequest request) {
        String key = "point:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if (!acquiredLock) {
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            pointFacadeService.tryReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    @PostMapping("/point/confirm")
    public void confirm(@RequestBody PointReserveConfirmRequest request) {
        String key = "point:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if (!acquiredLock) {
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            pointFacadeService.confirmReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }

    int count = 0;

    @PostMapping("/point/cancel")
    public void cancel(@RequestBody PointReserveCancelRequest request) throws InterruptedException {
        System.out.println("진입!!!");
        if(count % 2 == 0) {
            count++;
            // 타임아웃 테스트
//            Thread.sleep(2000);

            throw new RuntimeException("테스트를 위한 오류!");
        }

        String key = "point:" + request.requestId();
        boolean acquiredLock = redisLockService.tryLock(key, request.requestId());

        if (!acquiredLock) {
            throw new RuntimeException("락 획득에 실패하였습니다.");
        }

        try {
            pointFacadeService.cancelReserve(request.toCommand());
        } finally {
            redisLockService.releaseLock(key);
        }
    }
}
