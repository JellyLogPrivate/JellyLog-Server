package com.saram.jellylog.domain.pet.dto.request;

public record UserPetCreateRequest(
    Long userCode,
    Long petCode,
    Integer level,
    Long exp,
    String emotion
) {
}

