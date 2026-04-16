package com.saram.jellylog.food.dto.response;

import java.time.LocalDateTime;

public record UserFoodResponse(
    Long userFoodCode,
    Long userCode,
    Long foodCode,
    Integer quantity,
    LocalDateTime updatedAt
) {
}

