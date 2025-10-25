package com.example.point.init;


import com.example.point.domain.Point;
import com.example.point.intrastructure.PointRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataCreator {
    private final PointRepository pointRepository;

    @PostConstruct
    public void createTestData() {
        pointRepository.save(new Point(1L, 10000L));
    }
}
