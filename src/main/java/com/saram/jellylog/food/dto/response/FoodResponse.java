package com.saram.jellylog.domain.food.dto.response;

public record FoodResponse(
    Long foodCode,
    String foodName,
    String foodImage,
    Long foodPrice,
    Long foodExp
) {
}

