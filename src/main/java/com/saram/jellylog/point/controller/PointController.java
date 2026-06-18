package com.saram.jellylog.point.controller;

import com.saram.jellylog.point.dto.PointRequest;
import com.saram.jellylog.point.dto.PointResponse;
import com.saram.jellylog.point.service.PointService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<Page<PointResponse>> getPoints(Pageable pageable) {
        return ResponseEntity.ok(pointService.getPoints(pageable));
    }

    @GetMapping("/{pointLogCode}")
    public ResponseEntity<PointResponse> getPoint(@PathVariable Long pointLogCode) {
        return ResponseEntity.ok(pointService.getPoint(pointLogCode));
    }

    @GetMapping("/users")
    public ResponseEntity<List<PointResponse>> getUserPoints(@AuthenticationPrincipal Long userCode) {
        return ResponseEntity.ok(pointService.getUserPoints(userCode));
    }

    @PostMapping
    public ResponseEntity<PointResponse> createPoint(@RequestBody PointRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pointService.createPoint(request));
    }
}
