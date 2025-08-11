package com.vinny.backend.User.dto;

import com.vinny.backend.User.validation.annotation.ExistingBrandIds;
import com.vinny.backend.User.validation.annotation.ExistingRegionIds;
import com.vinny.backend.User.validation.annotation.ExistingVintageItemIds;
import com.vinny.backend.User.validation.annotation.ExistingVintageStyleIds;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OnboardingRequestDto {

    @NotEmpty(message = "닉네임은 필수입니다.")
    @Size(max = 50, message = "닉네임은 50자 이하로 입력해주세요.")
    private String nickname; // 닉네임

    @Size(max = 255, message = "코멘트는 255자 이하로 입력해주세요.")
    private String comment; // 코멘트

    @NotEmpty(message = "선호하는 빈티지 스타일을 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 3, message = "선호하는 빈티지 스타일은 최대 3개까지 선택할 수 있습니다.")
    @ExistingVintageStyleIds
    private List<Long> vintageStyleIds;

    @NotEmpty(message = "선호하는 브랜드를 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 5, message = "선호하는 브랜드는 최대 5개까지 선택할 수 있습니다.")
    @ExistingBrandIds
    private List<Long> brandIds;

    @NotEmpty(message = "선호하는 빈티지 아이템을 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 3, message = "선호하는 빈티지 아이템은 최대 3개까지 선택할 수 있습니다.")
    @ExistingVintageItemIds
    private List<Long> vintageItemIds;

    @NotEmpty(message = "주요 활동 지역을 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 3, message = "주요 활동 지역은 최대 3개까지 선택할 수 있습니다.")
    @ExistingRegionIds
    private List<Long> regionIds;
}
