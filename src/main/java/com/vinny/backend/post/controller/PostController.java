package com.vinny.backend.post.controller;

import com.vinny.backend.post.dto.PostSearchResponseDto;
import com.vinny.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
}
