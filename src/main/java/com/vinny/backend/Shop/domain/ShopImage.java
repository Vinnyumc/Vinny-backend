package com.vinny.backend.Shop.domain;

import com.vinny.backend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ShopImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Shop 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

//    @Column(name = "is_main_image", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0") // MYSQL 용
//    private boolean IsMainImage;
    @Column(name = "is_main_image", nullable = false) // H2 용
    private boolean isMainImage = false;

}