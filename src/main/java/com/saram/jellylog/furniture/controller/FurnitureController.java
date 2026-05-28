package com.saram.jellylog.furniture.controller;

import com.saram.jellylog.furniture.dto.request.FurnitureCreateRequest;
import com.saram.jellylog.furniture.dto.request.FurnitureUpdateRequest;
import com.saram.jellylog.furniture.dto.response.FurnitureResponse;
import com.saram.jellylog.furniture.service.FurnitureService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FurnitureController {

    private final FurnitureService furnitureService;

    public FurnitureController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    @GetMapping("/furnitures_all")
    public ResponseEntity<List<FurnitureResponse>> getFurnituresAll(
            @AuthenticationPrincipal Long userCode
    ) {
        return ResponseEntity.ok(furnitureService.getFurnitures(userCode));
    }

    @GetMapping("/furnitures_single/{furnitureCode}")
    public ResponseEntity<FurnitureResponse> getFurnitureSingle(
            @AuthenticationPrincipal Long userCode,
            @PathVariable Long furnitureCode
    ) {
        return ResponseEntity.ok(furnitureService.getFurniture(userCode, furnitureCode));
    }

    @PostMapping("/furnitures")
    public ResponseEntity<FurnitureResponse> createFurniture(
            @AuthenticationPrincipal Long userCode,
            @RequestBody FurnitureCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(furnitureService.createFurniture(userCode, request));
    }

    @PutMapping("/furnitures/{furnitureCode}")
    public ResponseEntity<FurnitureResponse> updateFurniture(
            @AuthenticationPrincipal Long userCode,
            @PathVariable Long furnitureCode,
            @RequestBody FurnitureUpdateRequest request
    ) {
        return ResponseEntity.ok(furnitureService.updateFurniture(userCode, furnitureCode, request));
    }

    @DeleteMapping("/furnitures/{furnitureCode}")
    public ResponseEntity<Void> deleteFurniture(
            @AuthenticationPrincipal Long userCode,
            @PathVariable Long furnitureCode
    ) {
        furnitureService.deleteFurniture(userCode, furnitureCode);
        return ResponseEntity.noContent().build();
    }
}