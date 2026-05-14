package com.saram.jellylog.user.service;

import com.saram.jellylog.global.exception.NotFoundException;
import com.saram.jellylog.user.dto.ProfileRequest;
import com.saram.jellylog.user.dto.ProfileResponse;
import com.saram.jellylog.user.entity.User;
import com.saram.jellylog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userCode) {
        User user = getUser(userCode);
        return new ProfileResponse(user.getUserName());
    }

    public ProfileResponse updateProfile(Long userCode, ProfileRequest request) {
        User user = getUser(userCode);
        user.setUserName(request.getUserName());
        return new ProfileResponse(user.getUserName());
    }

    public User getUser(Long userCode) {
        return userRepository.findById(userCode)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }

}
