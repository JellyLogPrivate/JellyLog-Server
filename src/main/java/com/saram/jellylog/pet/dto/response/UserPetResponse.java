package com.saram.jellylog.domain.pet.dto.response;

import java.time.LocalDateTime;

public record UserPetResponse(
    Long userCode,
    Long petCode,
    Integer level,
    Long exp,
    String emotion,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

