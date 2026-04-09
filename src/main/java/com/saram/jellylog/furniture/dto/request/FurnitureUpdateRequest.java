package com.saram.jellylog.domain.furniture.dto.request;

public record FurnitureUpdateRequest(
    String furnitureName,
    String furnitureImage,
    Long furniturePrice,
    String furnitureGroup,
    Integer furniturePositionX,
    Integer furniturePositionY,
    Integer furnitureDepthIndex
) {
}

