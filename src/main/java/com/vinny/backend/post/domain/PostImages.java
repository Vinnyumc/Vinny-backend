package com.vinny.backend.post.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_images")
public class PostImages {

    @EmbeddedId
    private PostImagesId id;

    @MapsId("postId") // 복합키 중 postId 필드와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}