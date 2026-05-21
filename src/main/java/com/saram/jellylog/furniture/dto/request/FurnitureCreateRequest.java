package com.saram.jellylog.furniture.dto.request;

public record FurnitureCreateRequest(
    String furnitureName,
    String furnitureImage,
    Long furniturePrice,
    Long furnitureGroup
) {
}
