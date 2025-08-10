package com.vinny.backend.style.dto;

import com.vinny.backend.User.domain.VintageStyle;

public record StyleDto(Long styleId, String styleName) {
    public static StyleDto from(VintageStyle e) {
        return new StyleDto(e.getId(), e.getName());
    }
}
