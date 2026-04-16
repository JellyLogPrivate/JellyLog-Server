package com.saram.jellylog.food.dto.request;

public record FeedPetRequest(
    Long userCode,
    Long petCode,
    Long foodCode,
    Integer quantity
) {
}

