package com.vinny.backend.post.service;

import com.vinny.backend.post.dto.PostSearchResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    public List<PostSearchResponseDto.PostDto> searchPosts(String keyword) {
        return List.of();
    }
}
