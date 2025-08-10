package com.vinny.backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionRequestDto {

    private boolean autoRefresh = false;
}
