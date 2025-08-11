package com.vinny.backend.Mypage.dto;

import jakarta.validation.constraints.Size;

public record MypageUpdateProfileRequest(
        @Size(min = 1, max = 10, message = "닉네임은 1~10자입니다.")
        String nickname,

        @Size(max = 100, message = "코멘트는 최대 100자입니다.")
        String comment
) {}


