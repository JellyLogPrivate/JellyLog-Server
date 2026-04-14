package com.saram.jellylog.food.controller;

import com.saram.jellylog.food.dto.request.FoodCreateRequest;
import com.saram.jellylog.food.dto.request.FoodUpdateRequest;
import com.saram.jellylog.food.dto.response.FoodResponse;
import com.saram.jellylog.food.service.FoodService;
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
//음식  코드에 대한 CRUD 구현
@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> getFoods() {
        return ResponseEntity.ok(foodService.getFoods());
    }

    @GetMapping("/{foodCode}")
    public ResponseEntity<FoodResponse> getFood(@PathVariable Long foodCode) {
        return ResponseEntity.ok(foodService.getFood(foodCode));
    }

    @PostMapping
    public ResponseEntity<FoodResponse> createFood(@RequestBody FoodCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFood(request));
    }

    @PutMapping("/{foodCode}")
    public ResponseEntity<FoodResponse> updateFood(
        @PathVariable Long foodCode,
        @RequestBody FoodUpdateRequest request
    ) {
        return ResponseEntity.ok(foodService.updateFood(foodCode, request));
    }

    @DeleteMapping("/{foodCode}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long foodCode) {
        foodService.deleteFood(foodCode);
        return ResponseEntity.noContent().build();
    }
}

