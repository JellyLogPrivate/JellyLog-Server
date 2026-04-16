package com.saram.jellylog.pet.dto.response;

import com.saram.jellylog.pet.entity.Pet;

public record PetResponse(
    Long petCode,
    String petName,
    String petDefaultImage,
    String petActiveImage1,
    String petActiveImage2,
    String petActiveImage3,
    String petActiveImage4,
    String petActiveImage5,
    String petActiveImage6,
    String petActiveImage7,
    String petActiveImage8
) {
    public static PetResponse from(Pet pet) {
        return new PetResponse(
            pet.getPetCode(),
            pet.getPetName(),
            pet.getPetDefaultImage(),
            pet.getPetActiveImage1(),
            pet.getPetActiveImage2(),
            pet.getPetActiveImage3(),
            pet.getPetActiveImage4(),
            pet.getPetActiveImage5(),
            pet.getPetActiveImage6(),
            pet.getPetActiveImage7(),
            pet.getPetActiveImage8()
        );
    }
}

