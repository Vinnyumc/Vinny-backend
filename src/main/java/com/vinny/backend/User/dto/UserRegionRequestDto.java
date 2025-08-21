package com.vinny.backend.User.dto;

import com.vinny.backend.User.validation.annotation.ExistingRegionIds;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegionRequestDto {

    @NotEmpty(message = "주요 활동 지역을 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 3, message = "주요 활동 지역은 최대 3개까지 선택할 수 있습니다.")
    @ExistingRegionIds
    private List<Long> regionIds;
}
