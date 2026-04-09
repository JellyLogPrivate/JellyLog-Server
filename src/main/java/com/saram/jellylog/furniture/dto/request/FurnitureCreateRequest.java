package com.saram.jellylog.domain.furniture.dto.request;

public record FurnitureCreateRequest(
    String furnitureName,
    String furnitureImage,
    Long furniturePrice,
    String furnitureGroup,
    Integer furniturePositionX,
    Integer furniturePositionY,
    Integer furnitureDepthIndex
) {
}

