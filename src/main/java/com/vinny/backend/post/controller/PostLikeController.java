package com.vinny.backend.post.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.post.service.PostLikeService;
import com.vinny.backend.search.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> addLike(
            @CurrentUser Long userId,
            @PathVariable Long postId
    ) {
        postLikeService.likePost(userId, postId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess("게시글 좋아요 등록에 성공했습니다.")
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> removeLike(
            @CurrentUser Long userId,
            @PathVariable Long postId
    ) {
        postLikeService.unlikePost(userId, postId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess("게시글 좋아요 취소에 성공했습니다.")
        );
    }
}
