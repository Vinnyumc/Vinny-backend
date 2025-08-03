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
import com.vinny.backend.post.dto.PostResponseDto;
import com.vinny.backend.post.dto.PostSearchResponseDto;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.s3.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ShopRepository shopRepository;
    private final VintageStyleRepository styleRepository;
    private final S3Service s3Service;

    public List<PostSearchResponseDto.PostDto> searchPosts(String keyword) {
        List<Post> posts = postRepository.searchByKeyword(keyword);

        return posts.stream().map(post -> new PostSearchResponseDto.PostDto(
                post.getId(),
                post.getTitle(),
                post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl(),
                post.getLikes() != null ? post.getLikes().size() : 0,
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
    public PostResponseDto.CreatePostResponse createPost(
            Long userId,
            String title,
            String content,
            List<Long> styleIds,
            List<Long> brandIds,
            Long shopId,
            List<MultipartFile> images
    ) throws java.io.IOException {
        // 1. 사용자 조회

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 2. 게시글 기본 정보 세팅
        Post post = Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        // 3. 이미지 처리
        if (images != null) {
            if (images.size() > 5) {
                throw new IllegalArgumentException("이미지는 최대 5개까지 업로드할 수 있습니다. ");
            }

            List<String> imageUrls = s3Service.uploadFiles(images);

            for (int i = 0; i < imageUrls.size(); i++) {
                post.getImages().add(new PostImage(post, imageUrls.get(i), (long) i));
            }
        }

        // 4. 브랜드 처리 (선택적, 여러 개 가능)
        if (brandIds != null) {
            for (Long brandId : brandIds) {
                Brand brand = brandRepository.findById(brandId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드입니다."));
                PostBrandHashtag brandHashtag = PostBrandHashtag.builder()
                        .post(post)
                        .brand(brand)
                        .build();
                post.getBrandHashtags().add(brandHashtag);
            }
        }

        // 5. 샵 처리 (선택적)
        if (shopId != null) {
            Shop shop = shopRepository.findById(shopId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 샵입니다."));
            PostShopHashtag shopHashtag = PostShopHashtag.builder()
                    .post(post)
                    .shop(shop)
                    .build();
            post.getShopHashtags().add(shopHashtag);
        }

        // 6. 스타일 처리 (선택적, 여러 개 가능)
        if (styleIds != null) {
            for (Long styleId : styleIds) {
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


    @Transactional
    public Long updatePost(Long userId, Long postId, com.vinny.backend.post.dto.PostRequestDto.UpdateDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.POST_FORBIDDEN);
        }

        if (dto.getTitle() != null) post.setTitle(dto.getTitle());
        if (dto.getContent() != null) post.setContent(dto.getContent());

        if (dto.getBrandIds() != null) {
            post.clearBrandHashtags();
            for (Long brandId : dto.getBrandIds()) {
                Brand brand = brandRepository.findById(brandId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BRAND_NOT_FOUND));
                post.addBrandHashtag(brand);
            }
        }

        if (dto.getStyleIds() != null) {
            post.clearStyleHashtags();
            for (Long styleId : dto.getStyleIds()) {
                VintageStyle style = styleRepository.findById(styleId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.STYLE_NOT_FOUND));
                post.addStyleHashtag(style);
            }
        }

        if (dto.getShopId() != null) {
            post.clearShopHashtags();
            Shop shop = shopRepository.findById(dto.getShopId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));
            post.addShopHashtag(shop);
        }

        //필요시 구현 예정(아직 dto에 이미지관련 필드 x)
//        // 이미지 갱신
//        if (dto.getImages() != null) {
//            List<MultipartFile> images = dto.getImages();
//
//            if (images.size() > 5) {
//                throw new IllegalArgumentException("이미지는 최대 5개까지 업로드할 수 있습니다.");
//            }
//
//            List<String> imageUrls = s3Service.uploadFiles(images);
//            post.getImages().clear();
//
//            for (int i = 0; i < imageUrls.size(); i++) {
//                post.getImages().add(new PostImage(post, imageUrls.get(i), (long) i));
//            }
//        }

        return post.getId();
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.POST_FORBIDDEN);
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDto.PostDetailResponseDto getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findByIdWithAllRelations(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        boolean isLikedByMe = post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(userId));

        int likesCount = post.getLikes() != null ? post.getLikes().size() : 0;

        return PostConverter.toDetailDto(post, isLikedByMe, likesCount);
    }
}