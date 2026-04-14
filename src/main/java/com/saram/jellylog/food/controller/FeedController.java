package com.saram.jellylog.food.controller;

import com.saram.jellylog.food.dto.request.FeedPetRequest;
import com.saram.jellylog.food.service.FeedService;
import com.saram.jellylog.pet.dto.response.UserPetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//먹인 음식에 따라 반려동물의 경험치와 감정이 업데이트되는 API
@RestController
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping
    public ResponseEntity<UserPetResponse> feedPet(@RequestBody FeedPetRequest request) {
        return ResponseEntity.ok(feedService.feedPet(request));
    }
}

