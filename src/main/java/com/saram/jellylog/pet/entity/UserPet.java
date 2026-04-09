package com.saram.jellylog.domain.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_pet_table")
public class UserPet {

    @EmbeddedId
    private UserPetId id;

    @Column(name = "user_pet_level")
    private Integer userPetLevel;

    @Column(name = "user_pet_exp")
    private Long userPetExp;

    @Column(name = "user_pet_emotion")
    private String userPetEmotion;

    @Column(name = "user_pet_created_at")
    private LocalDateTime userPetCreatedAt;

    @Column(name = "user_pet_updated_at")
    private LocalDateTime userPetUpdatedAt;

    public UserPet() {
    }

    public UserPetId getId() {
        return id;
    }

    public void setId(UserPetId id) {
        this.id = id;
    }

    public Integer getUserPetLevel() {
        return userPetLevel;
    }

    public void setUserPetLevel(Integer userPetLevel) {
        this.userPetLevel = userPetLevel;
    }

    public Long getUserPetExp() {
        return userPetExp;
    }

    public void setUserPetExp(Long userPetExp) {
        this.userPetExp = userPetExp;
    }

    public String getUserPetEmotion() {
        return userPetEmotion;
    }

    public void setUserPetEmotion(String userPetEmotion) {
        this.userPetEmotion = userPetEmotion;
    }

    public LocalDateTime getUserPetCreatedAt() {
        return userPetCreatedAt;
    }

    public void setUserPetCreatedAt(LocalDateTime userPetCreatedAt) {
        this.userPetCreatedAt = userPetCreatedAt;
    }

    public LocalDateTime getUserPetUpdatedAt() {
        return userPetUpdatedAt;
    }

    public void setUserPetUpdatedAt(LocalDateTime userPetUpdatedAt) {
        this.userPetUpdatedAt = userPetUpdatedAt;
    }
}

