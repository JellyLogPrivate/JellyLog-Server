package com.saram.jellylog.food.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "food_table")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_code")
    private Long foodCode;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "food_image")
    private String foodImage;

    @Column(name = "food_price")
    private Long foodPrice;

    @Column(name = "food_exp")
    private Long foodExp;

    public Food(String foodName, String foodImage, Long foodPrice, Long foodExp) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodPrice = foodPrice;
        this.foodExp = foodExp;
    }

    public static Food create(String foodName, String foodImage, Long foodPrice, Long foodExp) {
        return new Food(foodName, foodImage, foodPrice, foodExp);
    }

    public void updateInfo(String foodName, String foodImage, Long foodPrice, Long foodExp) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.foodPrice = foodPrice;
        this.foodExp = foodExp;
    }
}

