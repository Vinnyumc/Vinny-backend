package com.vinny.backend.Mypage.Controller;

import com.vinny.backend.Mypage.Service.MypageService;
import com.vinny.backend.Mypage.dto.*;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.s3.service.S3Service;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Mypage", description = "마이페이지 관련 API")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final MypageService mypageService;
    private final S3Service s3Service;

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

    @PatchMapping
    @Operation(
            summary = "프로필 수정",
            description = "닉네임/코멘트만 부분 수정합니다. null 필드는 무시됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<MypageUserProfileDto>> updateMe(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @Valid @RequestBody MypageUpdateProfileRequest request
    ) {
        MypageUserProfileDto dto = mypageService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess(dto));
    }

    @PatchMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "프로필 이미지 변경(업로드 포함)",
            description = "파일을 바로 받아 S3에 업로드하고, 업로드된 URL로 프로필 이미지를 갱신합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 이미지 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<MypageUserProfileDto>> updateProfileImage(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestPart("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(
                            ErrorStatus.FILE_IS_EMPTY.getCode(),
                            ErrorStatus.FILE_IS_EMPTY.getMessage(),
                            null
                    )
            );
        }
        try {
            // 1) S3 업로드 -> URL 확보
            String fileUrl = s3Service.uploadFile(file);

            // 2) URL로 프로필 이미지 갱신
            MypageUserProfileDto dto =
                    mypageService.updateProfileImage(userId, new MypageUpdateProfileImageRequest(fileUrl));

            return ResponseEntity.ok(ApiResponse.onSuccess("프로필 이미지가 변경되었습니다.", dto));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.onFailure(
                            ErrorStatus.S3_UPLOAD_FAILED.getCode(),
                            ErrorStatus.S3_UPLOAD_FAILED.getMessage(),
                            null
                    )
            );
        }
    }

    @PatchMapping(value = "/background-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "프로필 배경 이미지 변경(업로드 포함)",
            description = "파일을 받아 S3에 업로드하고, 업로드된 URL로 프로필 배경 이미지를 갱신합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배경 이미지 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<ApiResponse<MypageUserProfileDto>> updateBackgroundImage(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @Parameter(description = "업로드할 배경 이미지 파일", required = true)
            @RequestPart("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(
                            ErrorStatus.FILE_IS_EMPTY.getCode(),
                            ErrorStatus.FILE_IS_EMPTY.getMessage(),
                            null
                    )
            );
        }
        try {
            // 1) S3 업로드 -> URL 확보
            String fileUrl = s3Service.uploadFile(file);

            // (선택) 기존 배경 이미지가 있다면 삭제 로직 고려
            // mypageService.deleteOldBackgroundImageIfExists(userId);

            // 2) URL로 배경 이미지 갱신
            MypageUserProfileDto dto =
                    mypageService.updateBackgroundImage(userId, new MypageUpdateProfileImageRequest(fileUrl));

            return ResponseEntity.ok(ApiResponse.onSuccess("프로필 배경 이미지가 변경되었습니다.", dto));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.onFailure(
                            ErrorStatus.S3_UPLOAD_FAILED.getCode(),
                            ErrorStatus.S3_UPLOAD_FAILED.getMessage(),
                            null
                    )
            );
        }
    }

    @GetMapping("/saved-posts/images")
    @Operation(
            summary = "저장한 게시글 첫 이미지 리스트 조회",
            description = "로그인 사용자가 저장(좋아요)한 게시글에서 각 게시글의 첫 번째 이미지 URL만 추출하여, 좋아요 시점 최신순으로 평평한 리스트로 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "저장한 게시글 첫 이미지 리스트 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<ApiResponse<List<MypageBookmarkPostResponse>>> getSavedPostsFirstImageList(
            @Parameter(hidden = true) @CurrentUser Long userId
    ) {
        List<MypageBookmarkPostResponse> response = mypageService.list(userId);
        log.info("list() userId={}", userId);
        return ResponseEntity.ok(ApiResponse.onSuccess("저장한 게시글 목록(1장 이미지 포함) 조회 성공", response));
    }
}
