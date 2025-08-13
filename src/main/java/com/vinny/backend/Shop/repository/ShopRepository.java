package com.vinny.backend.Shop.repository;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.Shop.dto.ShopResponseDto;
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
           OR LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Shop> searchByNameOrStyleOrRegion(@Param("keyword") String keyword);


    Page<Shop> findByShopVintageStyleList_VintageStyle_Name(String style, Pageable pageable);

    @Query("""
        SELECT DISTINCT s FROM Shop s 
        JOIN s.shopVintageStyleList svs 
        JOIN svs.vintageStyle vs 
        WHERE vs.name = :styleName 
        """)
    Page<Shop> findByStyle(@Param("styleName") String styleType, Pageable pageable);

    @Query("""
        SELECT DISTINCT s FROM Shop s 
        JOIN s.shopVintageStyleList svs 
        JOIN svs.vintageStyle vs 
        JOIN s.region r 
        WHERE vs.name = :styleName 
        AND r.name LIKE %:region% 
        AND s.status = 'OPEN'
        """)
    Page<Shop> findByStyleAndRegion(
            @Param("styleName") String styleType,
            @Param("region") String region,
            Pageable pageable);

    @Query("""
select new com.vinny.backend.Shop.dto.ShopResponseDto$HomeForYouThumbnailDto(
    s.id, s.name, s.openTime, s.closeTime, s.instagram,
    s.address
)
from Shop s
where s.id in :ids and s.status = com.vinny.backend.Shop.domain.enums.Status.OPEN
""")
    List<ShopResponseDto.HomeForYouThumbnailDto> findHomeForYouByIds(@Param("ids") List<Long> ids);


    @Query("select s.name from Shop s")
    List<String> findAllShopNames();


}
