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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PostResponseDto getAllPosts(Pageable pageable, Long currentUserId) {
        Pageable effectivePageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Post> posts = postRepository.findAllWithAssociations(effectivePageable);
        List<PostResponseDto.PostDto> postDtos = posts
                .map(post -> PostConverter.toDto(post, currentUserId))
                .toList();

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

    public PostResponseDto getAllPostsOrderByLikes(Pageable pageable, Long currentUserId) {
        // 1) 먼저 like 수 기준으로 정렬된 Post ID만 페이지네이션으로 가져온다
        Page<Long> idPage = postRepository.findPostIdsOrderByLikes(pageable);

        if (idPage.isEmpty()) {
            return PostResponseDto.builder()
                    .posts(List.of())
                    .pageInfo(PostResponseDto.PageInfoDto.builder()
                            .page(idPage.getNumber())
                            .size(idPage.getSize())
                            .totalPages(idPage.getTotalPages())
                            .totalElements(idPage.getTotalElements())
                            .build())
                    .build();
        }

        List<Long> ids = idPage.getContent();

        // 2) 연관관계 JOIN FETCH로 본문 로딩 (id IN …)
        List<Post> fetched = postRepository.findAllWithAssociationsByIdIn(ids);

        // 3) IN 절은 정렬이 보장되지 않으므로, idPage 순서대로 재정렬
        java.util.Map<Long, Integer> orderIndex = new java.util.HashMap<>();
        for (int i = 0; i < ids.size(); i++) orderIndex.put(ids.get(i), i);
        fetched.sort(java.util.Comparator.comparingInt(p -> orderIndex.getOrDefault(p.getId(), Integer.MAX_VALUE)));

        // 4) DTO 변환
        List<PostResponseDto.PostDto> postDtos = fetched.stream()
                .map(post -> PostConverter.toDto(post, currentUserId))
                .toList();

        // 5) PageInfo 구성은 idPage 메타데이터 사용
        PostResponseDto.PageInfoDto pageInfo = PostResponseDto.PageInfoDto.builder()
                .page(idPage.getNumber())
                .size(idPage.getSize())
                .totalPages(idPage.getTotalPages())
                .totalElements(idPage.getTotalElements())
                .build();

        return PostResponseDto.builder()
                .posts(postDtos)
                .pageInfo(pageInfo)
                .build();
    }



    @Transactional
    public PostResponseDto.CreatePostResponse createPost(
            Long userId,
            com.vinny.backend.post.dto.PostRequestDto.CreateDto dto,
            List<MultipartFile> images
    ) throws java.io.IOException {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 2. 게시글 기본 정보 세팅
        String title = dto.getTitle();
        String content = dto.getContent();
        List<Long> styleIds = dto.getStyleIds();
        List<Long> brandIds = dto.getBrandIds();
        Long shopId = dto.getShopId();

        Post post = Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        if (title == null || title.isBlank()) {
            throw new GeneralException(ErrorStatus.TITLE_REQUIRED);
        }

        if (content == null || content.isBlank()) {
            throw new GeneralException(ErrorStatus.CONTENT_REQUIRED);
        }

        // 3. 이미지 처리
        if (images != null) {
            if (images.size() > 5) {
                throw new GeneralException(ErrorStatus.IMAGE_LIMIT_EXCEEDED);
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
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BRAND_NOT_FOUND));
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
                    .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));
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
                        .orElseThrow(() -> new GeneralException(ErrorStatus.STYLE_NOT_FOUND));
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
//                throw new GeneralException(ErrorStatus.IMAGE_LIMIT_EXCEEDED);
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

        boolean isBookmarkedByMe = post.getBookmarks().stream()
                .anyMatch(Bookmark -> Bookmark.getUser().getId().equals(userId));

        return PostConverter.toDetailDto(post, isLikedByMe, likesCount, isBookmarkedByMe);
    }
}