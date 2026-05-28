package com.saram.jellylog.furniture.controller;

import com.saram.jellylog.furniture.dto.request.UserFurnitureCreateRequest;
import com.saram.jellylog.furniture.dto.request.UserFurnitureUpdateRequest;
import com.saram.jellylog.furniture.dto.response.UserFurnitureResponse;
import com.saram.jellylog.furniture.service.FurnitureInventoryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/furnitures-inventory")
public class FurnitureInventoryController {

    private final FurnitureInventoryService furnitureInventoryService;

    public FurnitureInventoryController(FurnitureInventoryService furnitureInventoryService) {
        this.furnitureInventoryService = furnitureInventoryService;
    }

    // 사용자의 전체 가구 인벤토리 조회
    @GetMapping
    public ResponseEntity<List<UserFurnitureResponse>> getUserFurnitureInventory(
            Authentication authentication
    ) {
        Long userCode = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(furnitureInventoryService.getUserFurnitureInventory(userCode));
    }

    // 인벤토리 내 특정 가구 단건 조회
    @GetMapping("/{furnitureCode}")
    public ResponseEntity<UserFurnitureResponse> getUserFurnitureItem(
            Authentication authentication,
            @PathVariable Long furnitureCode
    ) {
        Long userCode = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(furnitureInventoryService.getUserFurnitureItem(userCode, furnitureCode));
    }

    // 인벤토리에 가구 추가
    @PostMapping
    public ResponseEntity<UserFurnitureResponse> createUserFurniture(
            Authentication authentication,
            @RequestBody UserFurnitureCreateRequest request
    ) {
        Long userCode = (Long) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(furnitureInventoryService.createUserFurniture(userCode, request));
    }

    // 인벤토리 가구 상태 수정
    @PutMapping("/{furnitureCode}")
    public ResponseEntity<UserFurnitureResponse> updateUserFurniture(
            Authentication authentication,
            @PathVariable Long furnitureCode,
            @RequestBody UserFurnitureUpdateRequest request
    ) {
        Long userCode = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(
                furnitureInventoryService.updateUserFurniture(userCode, furnitureCode, request)
        );
    }

    // 인벤토리 가구 삭제
    @DeleteMapping("/{furnitureCode}")
    public ResponseEntity<Void> deleteUserFurniture(
            Authentication authentication,
            @PathVariable Long furnitureCode
    ) {
        Long userCode = (Long) authentication.getPrincipal();
        furnitureInventoryService.deleteUserFurniture(userCode, furnitureCode);
        return ResponseEntity.noContent().build();
    }
}