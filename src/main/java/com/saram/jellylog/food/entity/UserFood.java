package com.saram.jellylog.domain.food.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_food_table")
public class UserFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_food_code")
    private Long userFoodCode;

    @Column(name = "user_code", nullable = false)
    private Long userCode;

    @Column(name = "food_code", nullable = false)
    private Long foodCode;

    @Column(name = "user_food_quantity")
    private Integer userFoodQuantity;

    @Column(name = "user_food_updated_at")
    private LocalDateTime userFoodUpdatedAt;

    public UserFood() {
    }

    public Long getUserFoodCode() {
        return userFoodCode;
    }

    public void setUserFoodCode(Long userFoodCode) {
        this.userFoodCode = userFoodCode;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public Long getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(Long foodCode) {
        this.foodCode = foodCode;
    }

    public Integer getUserFoodQuantity() {
        return userFoodQuantity;
    }

    public void setUserFoodQuantity(Integer userFoodQuantity) {
        this.userFoodQuantity = userFoodQuantity;
    }

    public LocalDateTime getUserFoodUpdatedAt() {
        return userFoodUpdatedAt;
    }

    public void setUserFoodUpdatedAt(LocalDateTime userFoodUpdatedAt) {
        this.userFoodUpdatedAt = userFoodUpdatedAt;
    }
}

