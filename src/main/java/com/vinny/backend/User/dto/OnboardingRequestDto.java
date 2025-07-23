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

//    private String nickname;

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
