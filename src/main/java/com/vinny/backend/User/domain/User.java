package com.vinny.backend.User.domain;

import com.vinny.backend.Common.domain.BaseEntity;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.domain.mapping.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저아이디 (PK)

    @Column(name = "kakao_user_id", nullable = false, unique = true)
    private Long kakaoUserId; // 카카오유저아이디

    @Column(length = 50)
    private String nickname; // 닉네임

    @Column(length = 100)
    private String email; // 이메일

    @Column(name = "profile_image", length = 255)
    private String profileImage; // 프로필사진

    @Column(length = 255)
    private String comment; // 코멘트

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private UserStatus userStatus; // 유지상태

    @Column(name = "inactivate_date")
    private LocalDateTime inactivateDate; // 비활성시기
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBrand> userBrandList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVintageStyle> userVintageStyleList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVintageItem> userVintageItemList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRegion> userRegionList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchLog> searchLogList  = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAgreement> userAgreements = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserShop> userShopList = new ArrayList<>();

    @Column(name = "refresh_token")
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
