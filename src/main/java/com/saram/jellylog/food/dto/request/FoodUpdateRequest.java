package com.saram.jellylog.domain.food.dto.request;

public record FoodUpdateRequest(
    String foodName,
    String foodImage,
    Long foodPrice,
    Long foodExp
) {
}

