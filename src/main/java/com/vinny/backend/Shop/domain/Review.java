package com.vinny.backend.Shop.domain;
import com.vinny.backend.common.domain.BaseEntity;
import com.vinny.backend.User.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    // Shop 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    // User 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Review 이미지들
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewImage> images  = new ArrayList<>();

    public static Review create(String title, String content, Shop shop, User user, List<String> imageUrls) {
        Review review = Review.builder()
                .title(title)
                .content(content)
                .shop(shop)
                .user(user)
                .build();

        imageUrls.forEach(url -> {
            ReviewImage image = ReviewImage.builder()
                    .imageUrl(url)
                    .review(review)
                    .build();
            review.images.add(image);
        });

        return review;
    }

}
