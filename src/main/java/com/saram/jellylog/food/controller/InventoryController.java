package com.saram.jellylog.food.controller;

import com.saram.jellylog.food.dto.request.UserFoodCreateRequest;
import com.saram.jellylog.food.dto.request.UserFoodUpdateRequest;
import com.saram.jellylog.food.dto.response.UserFoodResponse;
import com.saram.jellylog.food.service.InventoryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//음식 인벤토리(사용자-음식 관계)에 대한 CRUD 구현
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{userCode}")
    public ResponseEntity<List<UserFoodResponse>> getUserInventory(@PathVariable Long userCode) {
        return ResponseEntity.ok(inventoryService.getUserInventory(userCode));
    }

    @GetMapping("/{userCode}/{foodCode}")
    public ResponseEntity<UserFoodResponse> getUserInventoryItem(
        @PathVariable Long userCode,
        @PathVariable Long foodCode
    ) {
        return ResponseEntity.ok(inventoryService.getUserInventoryItem(userCode, foodCode));
    }

    @PostMapping
    public ResponseEntity<UserFoodResponse> createUserInventory(@RequestBody UserFoodCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createUserInventory(request));
    }

    @PutMapping("/{userCode}/{foodCode}")
    public ResponseEntity<UserFoodResponse> updateUserInventory(
        @PathVariable Long userCode,
        @PathVariable Long foodCode,
        @RequestBody UserFoodUpdateRequest request
    ) {
        return ResponseEntity.ok(inventoryService.updateUserInventory(userCode, foodCode, request));
    }

    @DeleteMapping("/{userCode}/{foodCode}")
    public ResponseEntity<Void> deleteUserInventory(
        @PathVariable Long userCode,
        @PathVariable Long foodCode
    ) {
        inventoryService.deleteUserInventory(userCode, foodCode);
        return ResponseEntity.noContent().build();
    }
}

