package com.saram.jellylog.furniture.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_furniture_table")
public class UserFurniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_furniture_code")
    private Long userFurnitureCode;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "furniture_code", nullable = false)
    private Long furnitureCode;

    @Column(name = "user_furniture_is_placed")
    private Boolean userFurnitureIsPlaced;

    @Column(name = "user_furniture_updated_at")
    private LocalDateTime userFurnitureUpdatedAt;

    public UserFurniture() {
    }

    public Long getUserFurnitureCode() {
        return userFurnitureCode;
    }

    public void setUserFurnitureCode(Long userFurnitureCode) {
        this.userFurnitureCode = userFurnitureCode;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public Long getFurnitureCode() {
        return furnitureCode;
    }

    public void setFurnitureCode(Long furnitureCode) {
        this.furnitureCode = furnitureCode;
    }

    public Boolean getUserFurnitureIsPlaced() {
        return userFurnitureIsPlaced;
    }

    public void setUserFurnitureIsPlaced(Boolean userFurnitureIsPlaced) {
        this.userFurnitureIsPlaced = userFurnitureIsPlaced;
    }

    public LocalDateTime getUserFurnitureUpdatedAt() {
        return userFurnitureUpdatedAt;
    }

    public void setUserFurnitureUpdatedAt(LocalDateTime userFurnitureUpdatedAt) {
        this.userFurnitureUpdatedAt = userFurnitureUpdatedAt;
    }
}
