package com.saram.jellylog.point.service;

import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
import com.saram.jellylog.point.dto.PointRequest;
import com.saram.jellylog.point.dto.PointResponse;
import com.saram.jellylog.point.entity.Point;
import com.saram.jellylog.point.repository.PointRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Transactional(readOnly = true)
    public List<PointResponse> getPoints() {
        return pointRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PointResponse getPoint(Long pointLogCode) {
        Point point = pointRepository.findById(pointLogCode)
            .orElseThrow(() -> new NotFoundException("Point log not found."));
        return toResponse(point);
    }

    @Transactional(readOnly = true)
    public List<PointResponse> getUserPoints(Long userCode) {
        return pointRepository.findByUserCodeOrderByPointLogCreatedAtDescPointLogCodeDesc(userCode)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public PointResponse createPoint(PointRequest request) {
        validatePointAmount(request.pointLogPointAmount());

        Point point = Point.create(
            request.userCode(),
            request.pointLogPointAmount(),
            request.pointLogReason(),
            request.pointLogReasonType()
        );

        return toResponse(pointRepository.save(point));
    }

    private void validatePointAmount(Integer pointAmount) {
        if (pointAmount == null || pointAmount == 0) {
            throw new ConflictException("Point amount must not be zero.");
        }
    }

    private PointResponse toResponse(Point point) {
        return new PointResponse(
            point.getPointLogCode(),
            point.getUserCode(),
            point.getPointLogPointAmount(),
            point.getPointLogReason(),
            point.getPointLogReasonType(),
            point.getPointLogCreatedAt()
        );
    }
}

