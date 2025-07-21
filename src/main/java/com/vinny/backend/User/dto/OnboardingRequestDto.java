package com.vinny.backend.User.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OnboardingRequestDto {

    private String nickname;
    private List<Long> vintageStyleIds;
    private List<Long> brandIds;
    private List<Long> vintageItemIds;
    private List<Long> regionIds;
}
