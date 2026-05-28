package com.saram.jellylog.furniture.service;

import com.saram.jellylog.furniture.dto.request.UserFurnitureCreateRequest;
import com.saram.jellylog.furniture.dto.request.UserFurnitureUpdateRequest;
import com.saram.jellylog.furniture.dto.response.UserFurnitureResponse;
import com.saram.jellylog.furniture.entity.UserFurniture;
import com.saram.jellylog.furniture.repository.FurnitureRepository;
import com.saram.jellylog.furniture.repository.UserFurnitureRepository;
import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FurnitureInventoryService {

    private final UserFurnitureRepository userFurnitureRepository;
    private final FurnitureRepository furnitureRepository;

    public FurnitureInventoryService(
            UserFurnitureRepository userFurnitureRepository,
            FurnitureRepository furnitureRepository
    ) {
        this.userFurnitureRepository = userFurnitureRepository;
        this.furnitureRepository = furnitureRepository;
    }

    @Transactional(readOnly = true)
    public List<UserFurnitureResponse> getUserFurnitureInventory(Long userCode) {
        return userFurnitureRepository.findByUserCode(userCode).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserFurnitureResponse getUserFurnitureItem(Long userCode, Long furnitureCode) {
        UserFurniture userFurniture = userFurnitureRepository
                .findByUserCodeAndFurnitureCode(userCode, furnitureCode)
                .orElseThrow(() -> new NotFoundException("Furniture inventory item not found."));
        return toResponse(userFurniture);
    }

    public UserFurnitureResponse createUserFurniture(Long userCode, UserFurnitureCreateRequest request) {
        validateFurnitureExists(request.furnitureCode());

        // ⭕ 변경: request.userCode() 대신 인증된 userCode 변수로 중복 체크를 수행합니다.
        if (userFurnitureRepository.existsByUserCodeAndFurnitureCode(userCode, request.furnitureCode())) {
            throw new ConflictException("Furniture inventory item already exists.");
        }

        // ⭕ 변경: 엔티티 저장 시에도 컨트롤러가 넘겨준 확실한 userCode를 바인딩합니다.
        UserFurniture userFurniture = UserFurniture.create(
                userCode,
                request.furnitureCode(),
                request.isPlaced(),
                LocalDateTime.now()
        );

        return toResponse(userFurnitureRepository.save(userFurniture));
    }

    public UserFurnitureResponse updateUserFurniture(
            Long userCode,
            Long furnitureCode,
            UserFurnitureUpdateRequest request
    ) {
        UserFurniture userFurniture = userFurnitureRepository
                .findByUserCodeAndFurnitureCode(userCode, furnitureCode)
                .orElseThrow(() -> new NotFoundException("Furniture inventory item not found."));

        userFurniture.updatePlaced(request.isPlaced(), LocalDateTime.now());

        return toResponse(userFurniture);
    }

    public void deleteUserFurniture(Long userCode, Long furnitureCode) {
        UserFurniture userFurniture = userFurnitureRepository
                .findByUserCodeAndFurnitureCode(userCode, furnitureCode)
                .orElseThrow(() -> new NotFoundException("Furniture inventory item not found."));
        userFurnitureRepository.delete(userFurniture);
    }

    private void validateFurnitureExists(Long furnitureCode) {
        if (!furnitureRepository.existsById(furnitureCode)) {
            throw new NotFoundException("Furniture not found.");
        }
    }

    private UserFurnitureResponse toResponse(UserFurniture userFurniture) {
        return new UserFurnitureResponse(
                userFurniture.getUserFurnitureCode(),
                userFurniture.getUserCode(),
                userFurniture.getFurnitureCode(),
                userFurniture.getUserFurnitureIsPlaced(),
                userFurniture.getUserFurnitureUpdatedAt()
        );
    }
}