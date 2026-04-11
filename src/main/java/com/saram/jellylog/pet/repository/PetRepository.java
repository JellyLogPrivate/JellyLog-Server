package com.saram.jellylog.domain.pet.repository;

import com.saram.jellylog.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // JpaRepository 메서드를 통해 petCode를 사용하여 반려동물 정보조회
}

