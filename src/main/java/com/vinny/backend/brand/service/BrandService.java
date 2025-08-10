
package com.vinny.backend.brand.service;

import com.vinny.backend.brand.dto.BrandDto;
import com.vinny.backend.User.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;

    /**
     * 키워드로 브랜드를 검색합니다. (부분 일치, 대소문자 무시)
     * 자동완성/간단 검색 용도로 상위 20개만 반환합니다.
     */
    public List<BrandDto> searchBrands(String keyword) {
        String term = keyword == null ? "" : keyword.trim();
        // 기본 크기 20, 이름 오름차순 정렬
        Pageable pageable = PageRequest.of(0, 20, Sort.by("name").ascending());
        return brandRepository
                .findByNameContainingIgnoreCaseOrderByNameAsc(term, pageable)
                .stream()
                .map(BrandDto::from)
                .toList();
    }
}
