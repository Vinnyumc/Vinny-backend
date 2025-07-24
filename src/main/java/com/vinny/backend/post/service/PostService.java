package com.vinny.backend.post.service;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.Brand;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.VintageStyle;
import com.vinny.backend.User.repository.BrandRepository;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.VintageStyleRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.post.converter.PostConverter;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.PostImage;
import com.vinny.backend.post.domain.mapping.PostBrandHashtag;
import com.vinny.backend.post.domain.mapping.PostShopHashtag;
import com.vinny.backend.post.domain.mapping.PostStyleHashtag;
import com.vinny.backend.post.dto.PostRequestDto;
import com.vinny.backend.post.dto.PostResponseDto;
import com.vinny.backend.post.dto.PostSearchResponseDto;
import com.vinny.backend.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ShopRepository shopRepository;
    private final VintageStyleRepository styleRepository;

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

    @Transactional
    public PostResponseDto.CreatePostResponse createPost(Long userId, PostRequestDto.CreatePostRequest request) {
        // 1. 사용자 조회

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 2. 게시글 기본 정보 세팅
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        // 3. 이미지 처리
        if (request.getImageUrls() != null) {
            if (request.getImageUrls().size() > 5) {
                throw new IllegalArgumentException("이미지는 최대 5개까지 업로드할 수 있습니다.");
            }
            List<PostImage> images = new ArrayList<>();
            List<String> imageUrls = request.getImageUrls();

            for (int i = 0; i < imageUrls.size(); i++) {
                String url = imageUrls.get(i);
                PostImage image = new PostImage(post, url, (long) i); // 순서를 Long으로 캐스팅
                images.add(image);
            }

            post.getImages().addAll(images);
        }

        // 4. 브랜드 처리 (선택적)
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
            PostBrandHashtag brandHashtag = PostBrandHashtag.builder()
                .post(post)
                .brand(brand)
                .build();
            post.getBrandHashtags().add(brandHashtag);
        }

        // 5. 샵 처리 (선택적)
        if (request.getShopId() != null) {
            Shop shop = shopRepository.findById(request.getShopId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 샵입니다."));
            PostShopHashtag shopHashtag = PostShopHashtag.builder()
                    .post(post)
                    .shop(shop)
                    .build();
            post.getShopHashtags().add(shopHashtag);
        }

        // 6. 스타일 처리 (선택적, 여러 개 가능)
        if (request.getStyleId() != null) {
            for (Long styleId : request.getStyleId()) {
                VintageStyle style = styleRepository.findById(styleId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스타일입니다."));
                PostStyleHashtag styleHashtag = PostStyleHashtag.builder()
                        .post(post)
                        .vintageStyle(style)
                        .build();
                post.getStyleHashtags().add(styleHashtag);
            }
        }

        // 7. 저장
        Post savedPost = postRepository.save(post);

        // 8. 응답 생성 및 반환
        return PostResponseDto.CreatePostResponse.builder()
                .postId(savedPost.getId())
                .build();
    }

}
