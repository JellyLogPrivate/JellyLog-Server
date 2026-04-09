package com.saram.jellylog.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "user")
public class User {

    // 사용자 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;

    // 사용자 닉네임
    @Column(nullable = false, length = 50)
    private String userName;

    // 사용자 보유 포인트
    @Column(nullable = false)
    private Integer userPoints = 0;

    // OAuth 제공자
    @Column(nullable = false, length = 20)
    private String userAuthProvider;

    // OAuth 사용자 ID (유니크)
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String userAuthProviderId;

    // 알림 허용 여부 (0 or 1)
    @Column(nullable = false)
    private Integer userNotificationEnabled = 1;

    // 소리 허용 여부 (0 or 1)
    @Column(nullable = false)
    private Integer userSoundEnabled = 1;

    // 생성일
    @Column(nullable = false, updatable = false)
    private LocalDateTime userCreatedAt;

    // 수정일
    private LocalDateTime userUpdatedAt;

    // 생성 시 자동 설정
    @PrePersist
    public void onCreate() {
        this.userCreatedAt = LocalDateTime.now();
    }

    // 수정 시 자동 업데이트
    @PreUpdate
    public void onUpdate() {
        this.userUpdatedAt = LocalDateTime.now();
    }
}