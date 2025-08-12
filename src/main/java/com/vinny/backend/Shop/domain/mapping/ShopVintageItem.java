package com.vinny.backend.Shop.domain.mapping;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.VintageItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopVintageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintage_item_id", nullable = false)
    private VintageItem vintageItem;
}
