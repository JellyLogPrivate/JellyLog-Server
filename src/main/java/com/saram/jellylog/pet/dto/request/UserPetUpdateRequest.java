package com.saram.jellylog.domain.pet.dto.request;

public record UserPetUpdateRequest(
    Integer level,
    Long exp,
    String emotion
) {
}

