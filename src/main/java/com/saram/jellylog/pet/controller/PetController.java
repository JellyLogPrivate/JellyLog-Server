package com.saram.jellylog.pet.controller;

import com.saram.jellylog.pet.dto.response.PetResponse;
import com.saram.jellylog.pet.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // 펫 마스터 전체 목록을 반환
    @GetMapping
    public ResponseEntity<Page<PetResponse>> getPets(Pageable pageable) {
        return ResponseEntity.ok(petService.getPets(pageable));
    }

    // petCode에 해당하는 펫 마스터 단건을 반환
    @GetMapping("/{petCode}")
    public ResponseEntity<PetResponse> getPet(@PathVariable Long petCode) {
        return ResponseEntity.ok(petService.getPet(petCode));
    }
}
