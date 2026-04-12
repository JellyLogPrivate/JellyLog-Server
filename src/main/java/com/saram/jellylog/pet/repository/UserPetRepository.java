package com.saram.jellylog.domain.pet.repository;

import com.saram.jellylog.domain.pet.entity.UserPet;
import com.saram.jellylog.domain.pet.entity.UserPetId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPetRepository extends JpaRepository<UserPet, UserPetId> {

    // 한 사용자가 소유한 모든 반려동물을 반환
    List<UserPet> findByIdUserCode(Long userCode);

    // 복합 키 필드를 사용하여 사용자-반려동물 관계를 하나 조회
    Optional<UserPet> findByIdUserCodeAndIdPetCode(Long userCode, Long petCode);
}

