package com.saram.jellylog.food.repository;

import com.saram.jellylog.food.entity.UserFood;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
// 음식 간의 관계를 관리 UserFood 엔티티에 대한 JPA 리포지토리 인터페이스
public interface UserFoodRepository extends JpaRepository<UserFood, Long> {

    List<UserFood> findByUserCode(Long userCode);

    Optional<UserFood> findByUserCodeAndFoodCode(Long userCode, Long foodCode);

    boolean existsByUserCodeAndFoodCode(Long userCode, Long foodCode);
}

