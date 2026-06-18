package com.saram.jellylog.pet.service;

import com.saram.jellylog.pet.dto.request.UserPetCreateRequest;
import com.saram.jellylog.pet.dto.request.UserPetUpdateRequest;
import com.saram.jellylog.pet.dto.response.PetResponse;
import com.saram.jellylog.pet.dto.response.UserPetResponse;
import com.saram.jellylog.pet.entity.UserPet;
import com.saram.jellylog.pet.entity.UserPetId;
import com.saram.jellylog.pet.repository.PetRepository;
import com.saram.jellylog.pet.repository.UserPetRepository;
import com.saram.jellylog.global.exception.ConflictException;
import com.saram.jellylog.global.exception.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Page; // 추가
import org.springframework.data.domain.Pageable; // 추가
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

    @Transactional(readOnly = true)
    public Page<PetResponse> getPets(Pageable pageable) {
        return petRepository.findAll(pageable)
                .map(PetResponse::from);
    }
    @Transactional(readOnly = true)
    public PetResponse getPet(Long petCode) {
        return PetResponse.from(
            petRepository.findById(petCode)
                .orElseThrow(() -> new NotFoundException("Pet master not found."))
        );
    }

    public UserPetResponse createUserPet(UserPetCreateRequest request) {
        validatePetExists(request.petCode());

        UserPetId id = new UserPetId(request.userCode(), request.petCode());
        if (userPetRepository.existsById(id)) {
            throw new ConflictException("User pet already exists.");
        }

        UserPet userPet = UserPet.create(id, request.level(), request.exp(), request.emotion());

        return UserPetResponse.from(userPetRepository.save(userPet));
    }

    @Transactional(readOnly = true)
    public UserPetResponse getUserPet(Long userCode, Long petCode) {
        UserPet userPet = userPetRepository.findByIdUserCodeAndIdPetCode(userCode, petCode)
            .orElseThrow(() -> new NotFoundException("User pet not found."));
        return UserPetResponse.from(userPet);
    }

    @Transactional(readOnly = true)
    public List<UserPetResponse> getUserPets(Long userCode) {
        return userPetRepository.findByIdUserCode(userCode)
            .stream()
            .map(UserPetResponse::from)
            .toList();
    }

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
