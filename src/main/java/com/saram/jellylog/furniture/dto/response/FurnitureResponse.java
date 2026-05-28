package com.saram.jellylog.furniture.dto.response;

public record FurnitureResponse(
        Long furnitureCode,
        String furnitureName,
        String furnitureImage,
        Integer furniturePrice,
        Integer furnitureGroup

) {
}
