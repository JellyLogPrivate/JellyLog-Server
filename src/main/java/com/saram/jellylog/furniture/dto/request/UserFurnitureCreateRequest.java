package com.saram.jellylog.furniture.dto.request;

public record UserFurnitureCreateRequest(
    Long userCode,
    Long furnitureCode,
    Boolean isPlaced
) {
}
