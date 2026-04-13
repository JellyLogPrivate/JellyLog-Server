package com.saram.jellylog.domain.food.service;

import com.saram.jellylog.domain.food.dto.request.FeedPetRequest;
import com.saram.jellylog.domain.food.entity.Food;
import com.saram.jellylog.domain.food.entity.UserFood;
import com.saram.jellylog.domain.food.repository.FoodRepository;
import com.saram.jellylog.domain.food.repository.UserFoodRepository;
import com.saram.jellylog.domain.pet.dto.response.UserPetResponse;
import com.saram.jellylog.domain.pet.entity.UserPet;
import com.saram.jellylog.domain.pet.repository.UserPetRepository;
import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@Transactional
public class FeedService {
    // 서 반려동물에게 음식을 먹이는 로직구현
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
    //  사용자의 인벤토리에서 해당 음식의 수량을 조회, 반려동물의 경험치와 레벨을 업데이트
    public UserPetResponse feedPet(FeedPetRequest request) {
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new ConflictException("Feed quantity must be positive.");
        }

        UserFood userFood = userFoodRepository.findByUserCodeAndFoodCode(request.userCode(), request.foodCode())
            .orElseThrow(() -> new NotFoundException("Inventory item not found."));

        int currentQty = userFood.getUserFoodQuantity() == null ? 0 : userFood.getUserFoodQuantity();
        // 음식 수량이 부족한 경우나 인벤토리에 해당 음식이 없는 경우에는 예외처리
        if (currentQty < request.quantity()) {
            throw new ConflictException("Not enough food quantity.");
        }

        Food food = foodRepository.findById(request.foodCode())
            .orElseThrow(() -> new NotFoundException("Food not found."));

        UserPet userPet = userPetRepository.findByIdUserCodeAndIdPetCode(request.userCode(), request.petCode())
            .orElseThrow(() -> new NotFoundException("User pet not found."));

        long foodExp = food.getFoodExp() == null ? 0L : food.getFoodExp();
        long gainedExp = foodExp * request.quantity();
        long currentExp = userPet.getUserPetExp() == null ? 0L : userPet.getUserPetExp();
        long nextExp = currentExp + gainedExp;

        int currentLevel = userPet.getUserPetLevel() == null ? 1 : userPet.getUserPetLevel();
        int calculatedLevel = (int) (nextExp / 100L) + 1;
        int nextLevel = Math.max(currentLevel, calculatedLevel);

        userPet.updateStatus(nextLevel, nextExp, "HAPPY");

        int remainingQty = currentQty - request.quantity();
        if (remainingQty == 0) {
            userFoodRepository.delete(userFood);
        } else {
            userFood.setUserFoodQuantity(remainingQty);
            userFood.setUserFoodUpdatedAt(LocalDateTime.now());
        }

        return UserPetResponse.from(userPet);
    }
}

