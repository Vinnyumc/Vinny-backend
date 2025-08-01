package com.vinny.backend.Mypage.Controller;

import com.vinny.backend.Mypage.Service.MypageService;
import com.vinny.backend.Mypage.dto.MypageLikedShopResponse;
import com.vinny.backend.Mypage.dto.MypagePostThumbnailResponse;
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

import java.util.List;

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


    // ========== 내가 작성한 게시글 목록 (대표 이미지 1장 포함) ==========
    @Operation(summary = "작성한 게시글 썸네일 목록 조회", description = "로그인한 사용자가 작성한 게시글들의 대표 이미지 1장을 포함한 썸네일 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<MypagePostThumbnailResponse>>> getMyPostsWithOneImage(
            @Parameter(hidden = true) @CurrentUser Long userId) {
        List<MypagePostThumbnailResponse> response = mypageService.getMyPostsWithOneImage(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess("작성한 게시글 목록(1장 이미지 포함) 조회 성공", response));
    }

    @Operation(
            summary = "찜한 샵 목록 조회",
            description = "로그인한 사용자가 찜한 상점 목록을 반환합니다. 상점 이름, 주소, 지역, 빈티지 스타일, 대표 이미지 1장을 포함합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "찜한 샵 목록 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/liked-shops")
    public ResponseEntity<ApiResponse<List<MypageLikedShopResponse>>> getLikedShops(
            @Parameter(hidden = true) @CurrentUser Long userId) {
        List<MypageLikedShopResponse> response = mypageService.getLikedShops(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess("찜한 샵 목록 조회 성공" ,response));
    }
}
