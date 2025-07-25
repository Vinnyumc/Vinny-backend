package com.vinny.backend.search.service;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.search.dto.PostResponse;
import com.vinny.backend.search.dto.StyleSearchRequest;
import com.vinny.backend.search.dto.UserSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostSearchService {

    private final PostRepository postRepository;

    public Page<PostResponse> searchByStyle(StyleSearchRequest request, Pageable pageable) {
        Page<Post> posts;

        if (request.keyword() != null && !request.keyword().trim().isEmpty()) {
            posts = postRepository.findByStyleAndKeyword(
                    request.styleType(), request.keyword(), pageable);
        } else {
            posts = postRepository.findByStyle(request.styleType(), pageable);
        }

        return posts.map(this::convertToResponse);
    }


    private PostResponse convertToResponse(Post post) {
        // User 정보를 UserSearchResponse로 변환
        UserSearchResponse author = UserSearchResponse.builder()
                .id(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .profileImage(post.getUser().getProfileImage())
                .comment(post.getUser().getComment())
                .build();

        // 이미지 URL 리스트
        List<String> imageUrls = post.getImages().stream()
                .map(img -> img.getImageUrl())
                .collect(Collectors.toList());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(author)
                .images(imageUrls)
                .styles(post.getStyleHashtags().stream()
                        .map(hashtag -> hashtag.getVintageStyle().getName())
                        .collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .totalImageCount(imageUrls.size())
                .likeCount(post.getLikes().size())
                .build();
    }
}