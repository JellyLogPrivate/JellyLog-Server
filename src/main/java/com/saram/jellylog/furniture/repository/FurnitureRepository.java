package com.saram.jellylog.furniture.repository;

import com.saram.jellylog.furniture.entity.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
}

