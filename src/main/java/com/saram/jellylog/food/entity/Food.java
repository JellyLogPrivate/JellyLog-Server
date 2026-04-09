package com.saram.jellylog.domain.food.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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
}

