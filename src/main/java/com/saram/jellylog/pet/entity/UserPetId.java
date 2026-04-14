package com.saram.jellylog.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPetId implements Serializable {

    @Column(name = "user_code")
    private Long userCode;

    @Column(name = "pet_code")
    private Long petCode;

    public UserPetId() {
    }

    public UserPetId(Long userCode, Long petCode) {
        this.userCode = userCode;
        this.petCode = petCode;
    }

    public Long getUserCode() {
        return userCode;
    }

    public Long getPetCode() {
        return petCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPetId that)) {
            return false;
        }
        return Objects.equals(userCode, that.userCode) && Objects.equals(petCode, that.petCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userCode, petCode);
    }
}

