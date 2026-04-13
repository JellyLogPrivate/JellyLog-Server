package com.saram.jellylog.domain.food.repository;

import com.saram.jellylog.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
// JpaRepository를 상속하여 음식정보로 기본적인 CRUD 메서드를 제공받음
public interface FoodRepository extends JpaRepository<Food, Long> {
}

