package com.vinny.backend.style.service;

import com.vinny.backend.style.dto.StyleDto;
import com.vinny.backend.style.repository.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StyleService {

    private final StyleRepository styleRepository;


    //이름 오름차순으로
    public List<StyleDto> getAllSorted() {
        return styleRepository.findAllByOrderByNameAsc()
                .stream()
                .map(StyleDto::from)
                .toList();
    }
}
