package com.saram.jellylog.domain.furniture.dto.request;

public record UserFurnitureCreateRequest(
    Long userCode,
    Long furnitureCode,
    Boolean isPlaced
) {
}

