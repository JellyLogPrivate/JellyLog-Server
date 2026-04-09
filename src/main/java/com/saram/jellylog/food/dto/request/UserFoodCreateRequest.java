package com.saram.jellylog.domain.food.dto.request;

public record UserFoodCreateRequest(
    Long userCode,
    Long foodCode,
    Integer quantity
) {
}

