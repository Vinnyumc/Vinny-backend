package com.vinny.backend.post.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.post.service.PostBookmarkService;
import com.vinny.backend.search.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/{postId}/bookmarks")
public class PostBookmarkController {

    private final PostBookmarkService postBookmarkService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> addBookmark(
            @CurrentUser Long userId,
            @PathVariable Long postId
    ) {
        postBookmarkService.bookmarkPost(userId, postId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess("게시글 북마크 등록에 성공했습니다.")
        );
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> removeBookmark(
            @CurrentUser Long userId,
            @PathVariable Long postId
    ) {
        postBookmarkService.unbookmarkPost(userId, postId);
        return ResponseEntity.ok(
                ApiResponse.onSuccess("게시글 북마크 취소에 성공했습니다.")
        );
    }
}
