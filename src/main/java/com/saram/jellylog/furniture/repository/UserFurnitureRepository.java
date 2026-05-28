package com.saram.jellylog.furniture.repository;

import com.saram.jellylog.furniture.entity.UserFurniture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFurnitureRepository extends JpaRepository<UserFurniture, Long> {

    List<UserFurniture> findByUserCode(Long userCode);

    Optional<UserFurniture> findByUserCodeAndFurnitureCode(Long userCode, Long furnitureCode);

    boolean existsByUserCodeAndFurnitureCode(Long userCode, Long furnitureCode);
}

