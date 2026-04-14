package com.saram.jellylog.food.dto.request;

public record UserFoodCreateRequest(
    Long userCode,
    Long foodCode,
    Integer quantity
) {
}

