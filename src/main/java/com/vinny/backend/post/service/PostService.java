package com.vinny.backend.post.service;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.dto.PostSearchResponseDto;
import com.vinny.backend.post.repsitory.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostSearchResponseDto.PostDto> searchPosts(String keyword) {
        List<Post> posts = postRepository.searchByKeyword(keyword);

        return posts.stream().map(post -> new PostSearchResponseDto.PostDto(
                post.getId(),
                post.getTitle(),
                post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl(),                post.getLikes() != null ? post.getLikes().size() : 0,
                false, // isLiked (Boolean) - 실 사용시 수정 칠요
                List.of(), //수정 필요
                post.getUser().getNickname()
        )).toList();

    }
}
