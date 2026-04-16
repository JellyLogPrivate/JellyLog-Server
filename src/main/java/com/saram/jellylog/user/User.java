package com.saram.jellylog.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="user_table")
public class User {

    // 사용자 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;

    // 사용자 닉네임 (별명)
    @Column(nullable = false, length = 50)
    @ColumnDefault("'사용자님'")
    private String userName = "'사용자님'";

    // 사용자 보유 포인트
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer userPoints = 0;

    // OAuth 제공자
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider userAuthProvider = AuthProvider.GOOGLE;

    // OAuth 사용자 ID
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String userAuthProviderId;

    // 알림 허용 여부 (0: false / 1: true)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private Boolean userNotificationEnabled = true;

    // 소리 허용 여부 (0: false / 1: true)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private Boolean userSoundEnabled = true;

    // 이메일 발송 허용 여부 (0: false / 1: true)
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private Boolean userEmailEnabled = true;

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