package com.vinny.backend.post.domain;

import com.vinny.backend.Shop.domain.Shop;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_shop_hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PostShopHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;
}
