package com.vinny.backend.Shop.domain;

import com.vinny.backend.Common.domain.BaseEntity;
import com.vinny.backend.Shop.domain.id.ShopStyleId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shop_style")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ShopStyle extends BaseEntity {

    @EmbeddedId
    private ShopStyleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shopId")
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("styleId")
    @JoinColumn(name = "style_id")
    private Style style;

}
