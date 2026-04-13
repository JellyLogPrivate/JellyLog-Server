package com.saram.jellylog.domain.food.service;

import com.saram.jellylog.domain.food.dto.request.FoodCreateRequest;
import com.saram.jellylog.domain.food.dto.request.FoodUpdateRequest;
import com.saram.jellylog.domain.food.dto.response.FoodResponse;
import com.saram.jellylog.domain.food.entity.Food;
import com.saram.jellylog.domain.food.repository.FoodRepository;
import com.saram.jellylog.global.exception.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// 음식 정보를 관리하는 서비스(음식의 CRUD)
@Service
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Transactional(readOnly = true)
    public List<FoodResponse> getFoods() {
        return foodRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FoodResponse getFood(Long foodCode) {
        Food food = foodRepository.findById(foodCode)
            .orElseThrow(() -> new NotFoundException("Food not found."));
        return toResponse(food);
    }

    public FoodResponse createFood(FoodCreateRequest request) {
        Food food = new Food();
        food.setFoodName(request.foodName());
        food.setFoodImage(request.foodImage());
        food.setFoodPrice(request.foodPrice());
        food.setFoodExp(request.foodExp());
        return toResponse(foodRepository.save(food));
    }

    public FoodResponse updateFood(Long foodCode, FoodUpdateRequest request) {
        Food food = foodRepository.findById(foodCode)
            .orElseThrow(() -> new NotFoundException("Food not found."));

        food.setFoodName(request.foodName());
        food.setFoodImage(request.foodImage());
        food.setFoodPrice(request.foodPrice());
        food.setFoodExp(request.foodExp());

        return toResponse(food);
    }

    public void deleteFood(Long foodCode) {
        if (!foodRepository.existsById(foodCode)) {
            throw new NotFoundException("Food not found.");
        }
        foodRepository.deleteById(foodCode);
    }

    private FoodResponse toResponse(Food food) {
        return new FoodResponse(
            food.getFoodCode(),
            food.getFoodName(),
            food.getFoodImage(),
            food.getFoodPrice(),
            food.getFoodExp()
        );
    }
}

