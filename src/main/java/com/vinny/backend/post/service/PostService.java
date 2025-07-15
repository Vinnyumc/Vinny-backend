package com.vinny.backend.post.service;

import com.vinny.backend.post.converter.PostConverter;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.dto.PostResponseDto;
import com.vinny.backend.post.dto.PostSearchResponseDto;
import com.vinny.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PostResponseDto getAllposts(Pageable pageable, Long currentUserId) {
//        //정렬 조건 추가
//        Pageable sortedPageable = PageRequest.of(
//                pageable.getPageNumber(),
//                pageable.getPageSize(),
//                Sort.by(Sort.Direction.DESC, "createdAt")
//        );

        Page<Post> posts = postRepository.findAllWithAssociations(pageable); // ← repository에서 JOIN FETCH
        List<PostResponseDto.PostDto> postDtos = posts.map(post -> PostConverter.toDto(post, currentUserId)).toList();

        PostResponseDto.PageInfoDto pageInfo = PostResponseDto.PageInfoDto.builder()
                .page(posts.getNumber())
                .size(posts.getSize())
                .totalPages(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();

        return PostResponseDto.builder()
                .posts(postDtos)
                .pageInfo(pageInfo)
                .build();
    }
}
