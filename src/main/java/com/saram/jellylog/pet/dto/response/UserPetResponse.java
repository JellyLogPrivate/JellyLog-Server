package com.saram.jellylog.domain.pet.dto.response;

import com.saram.jellylog.domain.pet.entity.UserPet;
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
    public static UserPetResponse from(UserPet userPet) {
        return new UserPetResponse(
            userPet.getId().getUserCode(),
            userPet.getId().getPetCode(),
            userPet.getUserPetLevel(),
            userPet.getUserPetExp(),
            userPet.getUserPetEmotion(),
            userPet.getUserPetCreatedAt(),
            userPet.getUserPetUpdatedAt()
        );
    }
}

