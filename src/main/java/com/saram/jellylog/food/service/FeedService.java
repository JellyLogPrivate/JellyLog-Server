package com.saram.jellylog.food.service;

import com.saram.jellylog.food.dto.request.FeedPetRequest;
import com.saram.jellylog.food.entity.Food;
import com.saram.jellylog.food.entity.UserFood;
import com.saram.jellylog.food.repository.FoodRepository;
import com.saram.jellylog.food.repository.UserFoodRepository;
import com.saram.jellylog.pet.dto.response.UserPetResponse;
import com.saram.jellylog.pet.entity.PetEmotion;
import com.saram.jellylog.pet.entity.UserPet;
import com.saram.jellylog.pet.repository.UserPetRepository;
import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@Transactional
public class FeedService {
    private static final long LEVEL_EXP_UNIT = 100L;

    private final UserFoodRepository userFoodRepository;
    private final FoodRepository foodRepository;
    private final UserPetRepository userPetRepository;

    public FeedService(
        UserFoodRepository userFoodRepository,
        FoodRepository foodRepository,
        UserPetRepository userPetRepository
    ) {
        this.userFoodRepository = userFoodRepository;
        this.foodRepository = foodRepository;
        this.userPetRepository = userPetRepository;
    }
    public UserPetResponse feedPet(FeedPetRequest request) {
        validateFeedQuantity(request.quantity());

        UserFood userFood = getUserFood(request.userCode(), request.foodCode());
        int currentQty = getCurrentQuantity(userFood);
        validateEnoughQuantity(currentQty, request.quantity());

        Food food = getFood(request.foodCode());
        UserPet userPet = getUserPet(request.userCode(), request.petCode());

        long nextExp = calculateNextExp(userPet, food, request.quantity());
        int nextLevel = calculateNextLevel(userPet, nextExp);

        userPet.updateStatus(nextLevel, nextExp, PetEmotion.HAPPY);
        decreaseInventory(userFood, currentQty, request.quantity());

        return UserPetResponse.from(userPet);
    }

    private void validateFeedQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new ConflictException("Feed quantity must be positive.");
        }
    }

    private UserFood getUserFood(Long userCode, Long foodCode) {
        return userFoodRepository.findByUserCodeAndFoodCode(userCode, foodCode)
            .orElseThrow(() -> new NotFoundException("Inventory item not found."));
    }

    private int getCurrentQuantity(UserFood userFood) {
        return userFood.getUserFoodQuantity() == null ? 0 : userFood.getUserFoodQuantity();
    }

    private void validateEnoughQuantity(int currentQty, int feedQty) {
        if (currentQty < feedQty) {
            throw new ConflictException("Not enough food quantity.");
        }
    }

    private Food getFood(Long foodCode) {
        return foodRepository.findById(foodCode)
            .orElseThrow(() -> new NotFoundException("Food not found."));
    }

    private UserPet getUserPet(Long userCode, Long petCode) {
        return userPetRepository.findByIdUserCodeAndIdPetCode(userCode, petCode)
            .orElseThrow(() -> new NotFoundException("User pet not found."));
    }

    private long calculateNextExp(UserPet userPet, Food food, int quantity) {
        long foodExp = food.getFoodExp() == null ? 0L : food.getFoodExp();
        long currentExp = userPet.getUserPetExp() == null ? 0L : userPet.getUserPetExp();
        return currentExp + (foodExp * quantity);
    }

    private int calculateNextLevel(UserPet userPet, long nextExp) {
        int currentLevel = userPet.getUserPetLevel() == null ? 1 : userPet.getUserPetLevel();
        int calculatedLevel = (int) (nextExp / LEVEL_EXP_UNIT) + 1;
        return Math.max(currentLevel, calculatedLevel);
    }

    private void decreaseInventory(UserFood userFood, int currentQty, int feedQty) {
        int remainingQty = currentQty - feedQty;
        if (remainingQty == 0) {
            userFoodRepository.delete(userFood);
            return;
        }
        userFood.updateQuantity(remainingQty, LocalDateTime.now());
    }
}

