package com.saram.jellylog.domain.pet.controller;

import com.saram.jellylog.domain.pet.dto.request.UserPetCreateRequest;
import com.saram.jellylog.domain.pet.dto.request.UserPetUpdateRequest;
import com.saram.jellylog.domain.pet.dto.response.UserPetResponse;
import com.saram.jellylog.domain.pet.service.PetService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-pets")
public class UserPetController {

    private final PetService petService;

    public UserPetController(PetService petService) {
        this.petService = petService;
    }

    // 사용자-반려동물 관계를 생성
    @PostMapping
    public ResponseEntity<UserPetResponse> createUserPet(@RequestBody UserPetCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.createUserPet(request));
    }

    // 사용자-반려동물 관계 하나를 반환
    @GetMapping("/{userCode}/{petCode}")
    public ResponseEntity<UserPetResponse> getUserPet(
        @PathVariable Long userCode,
        @PathVariable Long petCode
    ) {
        return ResponseEntity.ok(petService.getUserPet(userCode, petCode));
    }

    // 한 유저에 대한 모든 사용자-반려동물 관계를 반환
    @GetMapping("/users/{userCode}")
    public ResponseEntity<List<UserPetResponse>> getUserPets(@PathVariable Long userCode) {
        return ResponseEntity.ok(petService.getUserPets(userCode));
    }

    // 사용자와 반려동물 간의 관계 레벨/경험치/감정을 업데이트
    @PutMapping("/{userCode}/{petCode}")
    public ResponseEntity<UserPetResponse> updateUserPet(
        @PathVariable Long userCode,
        @PathVariable Long petCode,
        @RequestBody UserPetUpdateRequest request
    ) {
        return ResponseEntity.ok(petService.updateUserPet(userCode, petCode, request));
    }
}

