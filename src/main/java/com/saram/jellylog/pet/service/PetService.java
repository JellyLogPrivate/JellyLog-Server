package com.saram.jellylog.domain.pet.service;

import com.saram.jellylog.domain.pet.dto.request.UserPetCreateRequest;
import com.saram.jellylog.domain.pet.dto.request.UserPetUpdateRequest;
import com.saram.jellylog.domain.pet.dto.response.UserPetResponse;
import com.saram.jellylog.domain.pet.entity.UserPet;
import com.saram.jellylog.domain.pet.entity.UserPetId;
import com.saram.jellylog.domain.pet.repository.PetRepository;
import com.saram.jellylog.domain.pet.repository.UserPetRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetService {

    private final PetRepository petRepository;
    private final UserPetRepository userPetRepository;

    public PetService(PetRepository petRepository, UserPetRepository userPetRepository) {
        this.petRepository = petRepository;
        this.userPetRepository = userPetRepository;
    }

    // 반려동물 대상이 존재하는지 확인 후 사용자-반려동물 관계를 생성
    public UserPetResponse createUserPet(UserPetCreateRequest request) {
        validatePetExists(request.petCode());

        UserPetId id = new UserPetId(request.userCode(), request.petCode());
        if (userPetRepository.existsById(id)) {
            throw new IllegalArgumentException("User pet already exists.");
        }

        UserPet userPet = new UserPet();
        userPet.setId(id);
        userPet.setUserPetLevel(request.level());
        userPet.setUserPetExp(request.exp());
        userPet.setUserPetEmotion(request.emotion());
        userPet.setUserPetCreatedAt(LocalDateTime.now());

        return toResponse(userPetRepository.save(userPet));
    }

    // 사용자 코드와 반려동물 코드를 사용하여 사용자-반려동물 관계를 반환
    @Transactional(readOnly = true)
    public UserPetResponse getUserPet(Long userCode, Long petCode) {
        UserPet userPet = userPetRepository.findByIdUserCodeAndIdPetCode(userCode, petCode)
            .orElseThrow(() -> new IllegalArgumentException("User pet not found."));
        return toResponse(userPet);
    }

    // 사용자가 소유한 모든 반려동물을 반환
    @Transactional(readOnly = true)
    public List<UserPetResponse> getUserPets(Long userCode) {
        return userPetRepository.findByIdUserCode(userCode)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    // 기존 사용자-반려동물 관계의 변경 가능한 필드(레벨/경험치/감정)를 업데이트
    public UserPetResponse updateUserPet(Long userCode, Long petCode, UserPetUpdateRequest request) {
        UserPet userPet = userPetRepository.findByIdUserCodeAndIdPetCode(userCode, petCode)
            .orElseThrow(() -> new IllegalArgumentException("User pet not found."));

        userPet.setUserPetLevel(request.level());
        userPet.setUserPetExp(request.exp());
        userPet.setUserPetEmotion(request.emotion());
        userPet.setUserPetUpdatedAt(LocalDateTime.now());

        return toResponse(userPet);
    }

    private void validatePetExists(Long petCode) {
        if (!petRepository.existsById(petCode)) {
            throw new IllegalArgumentException("Pet master not found.");
        }
    }

    private UserPetResponse toResponse(UserPet userPet) {
        return new UserPetResponse(
            userPet.getId().getUserCode(),
            userPet.getId().getPetCode(),
            userPet.getUserPetLevel(),
            userPet.getUserPetExp(),
            userPet.getUserPetEmotion(),
            userPet.getUserPetCreatedAt(),
            userPet.getUserPetUpdatedAt()
        );
    }
}

