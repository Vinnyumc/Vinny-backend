package com.vinny.backend.User.dto;

import com.vinny.backend.User.validation.annotation.ExistingBrandIds;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserBrandRequestDto {

    @NotEmpty(message = "선호하는 브랜드를 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 5, message = "선호하는 브랜드는 최대 5개까지 선택할 수 있습니다.")
    @ExistingBrandIds
    private List<Long> brandIds;
}
