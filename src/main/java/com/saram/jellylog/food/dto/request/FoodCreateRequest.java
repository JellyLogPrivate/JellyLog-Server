package com.saram.jellylog.food.dto.request;

public record FoodCreateRequest(
    String foodName,
    String foodImage,
    Long foodPrice,
    Long foodExp
) {
}

/////////////