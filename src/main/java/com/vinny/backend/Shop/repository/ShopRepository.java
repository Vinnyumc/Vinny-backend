package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("""
        SELECT DISTINCT s FROM Shop s
        LEFT JOIN s.shopVintageStyleList svs
        LEFT JOIN svs.vintageStyle vs
        LEFT JOIN s.region r
        WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(vs.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Shop> searchByNameOrStyleOrRegion(@Param("keyword") String keyword);


    Page<Shop> findByShopVintageStyleList_VintageStyle_Name(String style, Pageable pageable);

}
