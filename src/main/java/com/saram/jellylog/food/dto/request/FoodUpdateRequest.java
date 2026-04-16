package com.saram.jellylog.food.dto.request;

public record FoodUpdateRequest(
    String foodName,
    String foodImage,
    Long foodPrice,
    Long foodExp
) {
}

