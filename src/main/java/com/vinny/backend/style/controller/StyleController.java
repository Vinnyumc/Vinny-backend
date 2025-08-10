package com.vinny.backend.style.controller;

import com.vinny.backend.error.ApiResponse;
import com.vinny.backend.style.dto.StyleDto;
import com.vinny.backend.style.service.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/style")
public class StyleController {

    private final StyleService styleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StyleDto>>> getStyles() {
        return ResponseEntity.ok(
                ApiResponse.onSuccess(styleService.getAllSorted())
        );
    }
}
