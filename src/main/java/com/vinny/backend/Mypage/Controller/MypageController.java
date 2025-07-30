package com.vinny.backend.Mypage.Controller;

import com.vinny.backend.Mypage.Service.MypageService;
import com.vinny.backend.Mypage.dto.MypageProfileResponse;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MypageProfileResponse>> getMyProfile(@CurrentUser Long userId) {
        MypageProfileResponse response = mypageService.getMyProfile(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess("프로필 정보를 조회했습니다.",response));
    }
}
