package com.vinny.backend.post.domain;

import com.vinny.backend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PostImage extends BaseEntity {

    @EmbeddedId
    private PostImageId id;

    @MapsId("postId") // 복합키 중 postId 필드와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;
}