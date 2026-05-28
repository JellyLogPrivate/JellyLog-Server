package com.saram.jellylog.furniture.controller;

import com.saram.jellylog.furniture.dto.request.UserFurnitureCreateRequest;
import com.saram.jellylog.furniture.dto.request.UserFurnitureUpdateRequest;
import com.saram.jellylog.furniture.dto.response.UserFurnitureResponse;
import com.saram.jellylog.furniture.service.FurnitureInventoryService;
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
@RequestMapping("/api/furnitures-inventory")
public class FurnitureInventoryController {

    private final FurnitureInventoryService furnitureInventoryService;

    public FurnitureInventoryController(FurnitureInventoryService furnitureInventoryService) {
        this.furnitureInventoryService = furnitureInventoryService;
    }

    // 사용자의 전체 가구 인벤토리 조회
    @GetMapping
    public ResponseEntity<List<UserFurnitureResponse>> getUserFurnitureInventory(
            @AuthenticationPrincipal Long userCode
    ) {
        return ResponseEntity.ok(furnitureInventoryService.getUserFurnitureInventory(userCode));
    }

    // 인벤토리 내 특정 가구 단건 조회
    @GetMapping("/{furnitureCode}")
    public ResponseEntity<UserFurnitureResponse> getUserFurnitureItem(
            @AuthenticationPrincipal Long userCode,
            @PathVariable Long furnitureCode
    ) {
        return ResponseEntity.ok(furnitureInventoryService.getUserFurnitureItem(userCode, furnitureCode));
    }

    // 인벤토리에 가구 추가 (구매 혹은 등록) - 누락되었던 인증 정보 추가
    @PostMapping
    public ResponseEntity<UserFurnitureResponse> createUserFurniture(
            @AuthenticationPrincipal Long userCode,
            @RequestBody UserFurnitureCreateRequest request
    ) {
        // 서비스 레이어의 메서드가 userCode를 함께 받도록 수정되어야 합니다.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(furnitureInventoryService.createUserFurniture(userCode, request));
    }

    // 인벤토리 가구 상태 수정 (배치 여부 전환 등)
    @PutMapping("/{furnitureCode}")
    public ResponseEntity<UserFurnitureResponse> updateUserFurniture(
            @AuthenticationPrincipal Long userCode,
            @PathVariable Long furnitureCode,
            @RequestBody UserFurnitureUpdateRequest request
    ) {
        return ResponseEntity.ok(
                furnitureInventoryService.updateUserFurniture(userCode, furnitureCode, request)
        );
    }

    // 인벤토리 가구 삭제 (소유권 해제 혹은 버리기)
    @DeleteMapping("/{furnitureCode}")
    public ResponseEntity<Void> deleteUserFurniture(
            @AuthenticationPrincipal Long userCode,
            @PathVariable Long furnitureCode
    ) {
        furnitureInventoryService.deleteUserFurniture(userCode, furnitureCode);
        return ResponseEntity.noContent().build();
    }
}