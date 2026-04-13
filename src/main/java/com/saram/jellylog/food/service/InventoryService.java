package com.saram.jellylog.domain.food.service;

import com.saram.jellylog.domain.food.dto.request.UserFoodCreateRequest;
import com.saram.jellylog.domain.food.dto.request.UserFoodUpdateRequest;
import com.saram.jellylog.domain.food.dto.response.UserFoodResponse;
import com.saram.jellylog.domain.food.entity.UserFood;
import com.saram.jellylog.domain.food.repository.FoodRepository;
import com.saram.jellylog.domain.food.repository.UserFoodRepository;
import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// 인벤토리 관리를 담당하는 서비스(사용자-음식 관계의 CRUD)
@Service
@Transactional
public class InventoryService {

    private final UserFoodRepository userFoodRepository;
    private final FoodRepository foodRepository;

    public InventoryService(UserFoodRepository userFoodRepository, FoodRepository foodRepository) {
        this.userFoodRepository = userFoodRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional(readOnly = true)
    public List<UserFoodResponse> getUserInventory(Long userCode) {
        return userFoodRepository.findByUserCode(userCode).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserFoodResponse getUserInventoryItem(Long userCode, Long foodCode) {
        UserFood userFood = userFoodRepository.findByUserCodeAndFoodCode(userCode, foodCode)
            .orElseThrow(() -> new NotFoundException("Inventory item not found."));
        return toResponse(userFood);
    }

    public UserFoodResponse createUserInventory(UserFoodCreateRequest request) {
        validateFoodExists(request.foodCode());

        if (userFoodRepository.existsByUserCodeAndFoodCode(request.userCode(), request.foodCode())) {
            throw new ConflictException("Inventory item already exists.");
        }

        UserFood userFood = new UserFood();
        userFood.setUserCode(request.userCode());
        userFood.setFoodCode(request.foodCode());
        userFood.setUserFoodQuantity(request.quantity());
        userFood.setUserFoodUpdatedAt(LocalDateTime.now());

        return toResponse(userFoodRepository.save(userFood));
    }

    public UserFoodResponse updateUserInventory(Long userCode, Long foodCode, UserFoodUpdateRequest request) {
        UserFood userFood = userFoodRepository.findByUserCodeAndFoodCode(userCode, foodCode)
            .orElseThrow(() -> new NotFoundException("Inventory item not found."));

        userFood.setUserFoodQuantity(request.quantity());
        userFood.setUserFoodUpdatedAt(LocalDateTime.now());

        return toResponse(userFood);
    }

    public void deleteUserInventory(Long userCode, Long foodCode) {
        UserFood userFood = userFoodRepository.findByUserCodeAndFoodCode(userCode, foodCode)
            .orElseThrow(() -> new NotFoundException("Inventory item not found."));
        userFoodRepository.delete(userFood);
    }

    private void validateFoodExists(Long foodCode) {
        if (!foodRepository.existsById(foodCode)) {
            throw new NotFoundException("Food not found.");
        }
    }

    private UserFoodResponse toResponse(UserFood userFood) {
        return new UserFoodResponse(
            userFood.getUserFoodCode(),
            userFood.getUserCode(),
            userFood.getFoodCode(),
            userFood.getUserFoodQuantity(),
            userFood.getUserFoodUpdatedAt()
        );
    }
}

