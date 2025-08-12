package com.vinny.backend.User.controller;

import com.vinny.backend.User.dto.UserPostSummaryDto;
import com.vinny.backend.User.dto.UserProfileDto;
import com.vinny.backend.User.service.UserPostService;
import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.search.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "User", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
public class UserPostController {

    private final UserPostService userPostService;

    @GetMapping("/api/users/{userId}/profile")
    public ResponseEntity<ApiResponse<?>> getUserProfile(@PathVariable Long userId) {
        UserProfileDto profile = userPostService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(profile));
    }

    @GetMapping("/api/users/{userId}/posts")
    public ResponseEntity<ApiResponse<?>> getUserPosts(@PathVariable Long userId) {
        List<UserPostSummaryDto> posts = userPostService.getUserPosts(userId);
        return ResponseEntity.ok(ApiResponse.onSuccess(posts));
    }
}
