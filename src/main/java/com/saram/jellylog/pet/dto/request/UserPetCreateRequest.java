package com.saram.jellylog.pet.dto.request;

public record UserPetCreateRequest(
    Long userCode,
    Long petCode,
    Integer level,
    Long exp,
    String emotion
) {
}

