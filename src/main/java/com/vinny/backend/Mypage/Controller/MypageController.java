package com.vinny.backend.Mypage.Controller;

import com.vinny.backend.Mypage.Service.MypageService;
import com.vinny.backend.Mypage.dto.MypageProfileResponse;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // ========== 마이페이지 프로필 정보 조회 ==========
    @Operation(summary = "마이페이지 프로필 정보 조회", description = "닉네임, 프로필 이미지, 코멘트, 게시글 수, 찜한 샵 수, 저장함 수 등을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 정보 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MypageProfileResponse>> getMyProfile(
            @Parameter(hidden = true) @CurrentUser Long userId) {
        MypageProfileResponse response = mypageService.getMyProfile(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess("프로필 정보를 조회했습니다.", response));
    }
}
