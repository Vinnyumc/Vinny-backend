package com.vinny.backend.Shop.domain.mapping;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.VintageStyle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "shop_vintage_style")
public class ShopVintageStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintage_style_id", nullable = false)
    private VintageStyle vintageStyle;
}