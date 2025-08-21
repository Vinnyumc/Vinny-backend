package com.vinny.backend.User.dto;

import com.vinny.backend.User.validation.annotation.ExistingVintageStyleIds;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserVintageStyleRequestDto {

    @NotEmpty(message = "선호하는 빈티지 스타일을 최소 1개 이상 선택해야 합니다.")
    @Size(min = 1, max = 3, message = "선호하는 빈티지 스타일은 최대 3개까지 선택할 수 있습니다.")
    @ExistingVintageStyleIds
    private List<Long> vintageStyleIds;
}

