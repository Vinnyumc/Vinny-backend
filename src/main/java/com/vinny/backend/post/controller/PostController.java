package com.vinny.backend.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.post.dto.PostRequestDto;
import com.vinny.backend.post.dto.PostResponseDto;
import com.vinny.backend.post.dto.PostSearchResponseDto;
import com.vinny.backend.post.service.PostService;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.io.IOException;

@RestController
@RequestMapping("/api/post")
@Tag(name = "Post", description = "커뮤니티 게시글 관련 API")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @Operation(summary = "게시글 검색", description = "키워드로 게시글을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword
    ) {
        List<PostSearchResponseDto.PostDto> posts = postService.searchPosts(keyword);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "게시글 검색 결과입니다.",
                        "status", 200,
                        "data", Map.of("posts", posts),
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @Operation(summary = "전체 피드 조회", description = "페이징 기반으로 전체 게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<PostResponseDto>> getAllPosts(
            // TODO: 추후 로그인 인증 구현 후 활성화
            // @Parameter(hidden = true)
            // @AuthenticationPrincipal CustomUserDetails userDetails,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // createdAt DESC 기준으로 정렬 고정
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // TODO: 로그인 인증 구현 전까지는 임시 userId 사용
        Long userId = 0L;

        PostResponseDto response = postService.getAllposts(pageable, userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성", description = "게시글 내용과 이미지 파일을 업로드합니다.")
    public ResponseEntity<ApiResponse<PostResponseDto.CreatePostResponse>> createPost(
            @CurrentUser Long userId,

            @Parameter(
                    description = "게시글 작성 JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PostRequestDto.CreateDto.class)
                    )
            )
            @RequestPart("dto") String dtoJson,

            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PostRequestDto.CreateDto dto = objectMapper.readValue(dtoJson, PostRequestDto.CreateDto.class);

        PostResponseDto.CreatePostResponse response =
                postService.createPost(userId, dto.getTitle(), dto.getContent(), dto.getStyleIds(), dto.getBrandIds(), dto.getShopId(), images);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess(response));
    }
}
