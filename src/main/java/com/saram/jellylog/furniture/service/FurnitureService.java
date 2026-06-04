package com.saram.jellylog.furniture.service;

import com.saram.jellylog.furniture.dto.request.FurnitureCreateRequest;
import com.saram.jellylog.furniture.dto.request.FurnitureUpdateRequest;
import com.saram.jellylog.furniture.dto.response.FurnitureResponse;
import com.saram.jellylog.furniture.entity.Furniture;
import com.saram.jellylog.furniture.repository.FurnitureRepository;
import com.saram.jellylog.global.exception.NotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FurnitureService {

    private final FurnitureRepository furnitureRepository;

    public FurnitureService(FurnitureRepository furnitureRepository) {
        this.furnitureRepository = furnitureRepository;
    }

    @Transactional(readOnly = true)
    public List<FurnitureResponse> getFurnitures(Long userCode) {
        return furnitureRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FurnitureResponse getFurniture(Long code,Long furnitureCode) {
        Furniture furniture = furnitureRepository.findById(furnitureCode)
            .orElseThrow(() -> new NotFoundException("Furniture not found."));
        return toResponse(furniture);
    }

    public FurnitureResponse createFurniture(Long userCode, FurnitureCreateRequest request) {
        Furniture furniture = Furniture.create(
            request.furnitureName(),
            request.furnitureImage(),
            request.furniturePrice(),
            request.furnitureGroup()
        );
        return toResponse(furnitureRepository.save(furniture));
    }

    public FurnitureResponse updateFurniture(Long code,Long furnitureCode, FurnitureUpdateRequest request) {
        Furniture furniture = furnitureRepository.findById(furnitureCode)
            .orElseThrow(() -> new NotFoundException("Furniture not found."));

        furniture.updateInfo(
            request.furnitureName(),
            request.furnitureImage(),
            request.furniturePrice(),
            request.furnitureGroup()
        );

        return toResponse(furniture);
    }

    public void deleteFurniture(Long code,Long furnitureCode) {
        if (!furnitureRepository.existsById(furnitureCode)) {
            throw new NotFoundException("Furniture not found.");
        }
        furnitureRepository.deleteById(furnitureCode);
    }

    private FurnitureResponse toResponse(Furniture furniture) {
        return new FurnitureResponse(
            furniture.getFurnitureCode(),
            furniture.getFurnitureName(),
            furniture.getFurnitureImage(),
            furniture.getFurniturePrice(),
            furniture.getFurnitureGroup()
        );
    }
}

