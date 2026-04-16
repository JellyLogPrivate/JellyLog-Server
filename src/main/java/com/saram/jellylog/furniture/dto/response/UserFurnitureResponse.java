package com.saram.jellylog.furniture.dto.response;

import java.time.LocalDateTime;

public record UserFurnitureResponse(
    Long userFurnitureCode,
    Long userCode,
    Long furnitureCode,
    Boolean isPlaced,
    LocalDateTime updatedAt
) {
}
