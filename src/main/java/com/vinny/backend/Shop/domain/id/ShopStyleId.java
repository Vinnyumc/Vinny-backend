package com.vinny.backend.Shop.domain.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ShopStyleId implements Serializable {

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "style_id")
    private Long styleId;
}
