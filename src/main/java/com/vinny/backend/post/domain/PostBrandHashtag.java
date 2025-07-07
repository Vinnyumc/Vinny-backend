package com.vinny.backend.post.domain;

import com.vinny.backend.User.domain.Brand;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_brand_hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PostBrandHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
}
