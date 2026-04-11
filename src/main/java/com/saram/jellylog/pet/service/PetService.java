package com.saram.jellylog.domain.pet.service;

import com.saram.jellylog.domain.pet.dto.request.UserPetCreateRequest;
import com.saram.jellylog.domain.pet.dto.request.UserPetUpdateRequest;
import com.saram.jellylog.domain.pet.dto.response.PetResponse;
import com.saram.jellylog.domain.pet.dto.response.UserPetResponse;
import com.saram.jellylog.domain.pet.entity.UserPet;
import com.saram.jellylog.domain.pet.entity.UserPetId;
import com.saram.jellylog.domain.pet.repository.PetRepository;
import com.saram.jellylog.domain.pet.repository.UserPetRepository;
import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
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

    // 펫 마스터 전체 목록을 반환
    @Transactional(readOnly = true)
    public List<PetResponse> getPets() {
        return petRepository.findAll().stream().map(PetResponse::from).toList();
    }

    // petCode에 해당하는 펫 마스터 한 건을 반환
    @Transactional(readOnly = true)
    public PetResponse getPet(Long petCode) {
        return PetResponse.from(
            petRepository.findById(petCode)
                .orElseThrow(() -> new NotFoundException("Pet master not found."))
        );
    }

    // 반려동물 대상이 존재하는지 확인 후 사용자-반려동물 관계를 생성
    public UserPetResponse createUserPet(UserPetCreateRequest request) {
        validatePetExists(request.petCode());

        UserPetId id = new UserPetId(request.userCode(), request.petCode());
        if (userPetRepository.existsById(id)) {
            throw new ConflictException("User pet already exists.");
        }

        UserPet userPet = UserPet.create(id, request.level(), request.exp(), request.emotion());

        return UserPetResponse.from(userPetRepository.save(userPet));
    }

    // 사용자 코드와 반려동물 코드를 사용하여 사용자-반려동물 관계를 반환
    @Transactional(readOnly = true)
    public UserPetResponse getUserPet(Long userCode, Long petCode) {
        UserPet userPet = userPetRepository.findByIdUserCodeAndIdPetCode(userCode, petCode)
            .orElseThrow(() -> new NotFoundException("User pet not found."));
        return UserPetResponse.from(userPet);
    }

    // 사용자가 소유한 모든 반려동물을 반환
    @Transactional(readOnly = true)
    public List<UserPetResponse> getUserPets(Long userCode) {
        return userPetRepository.findByIdUserCode(userCode)
            .stream()
            .map(UserPetResponse::from)
            .toList();
    }

    // 기존 사용자-반려동물 관계의 변경 가능한 필드(레벨/경험치/감정)를 업데이트
    public UserPetResponse updateUserPet(Long userCode, Long petCode, UserPetUpdateRequest request) {
        UserPet userPet = userPetRepository.findByIdUserCodeAndIdPetCode(userCode, petCode)
            .orElseThrow(() -> new NotFoundException("User pet not found."));

        userPet.updateStatus(request.level(), request.exp(), request.emotion());

        return UserPetResponse.from(userPet);
    }

    private void validatePetExists(Long petCode) {
        if (!petRepository.existsById(petCode)) {
            throw new NotFoundException("Pet master not found.");
        }
    }

}

