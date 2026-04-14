package com.saram.jellylog.pet.dto.request;

public record UserPetUpdateRequest(
    Integer level,
    Long exp,
    String emotion
) {
}

