package com.vinny.backend.Shop.dto;

import com.vinny.backend.User.domain.VintageStyle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopVintageStyleDto {
    private Long id;
    private String vintageStyleName;
}
