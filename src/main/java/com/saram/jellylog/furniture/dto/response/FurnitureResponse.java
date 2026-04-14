package com.saram.jellylog.furniture.dto.response;

public record FurnitureResponse(
    Long furnitureCode,
    String furnitureName,
    String furnitureImage,
    Long furniturePrice,
    String furnitureGroup,
    Integer furniturePositionX,
    Integer furniturePositionY,
    Integer furnitureDepthIndex
) {
}
