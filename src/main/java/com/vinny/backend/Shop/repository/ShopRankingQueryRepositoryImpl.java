package com.vinny.backend.Shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vinny.backend.Shop.domain.QShop;
import com.vinny.backend.Shop.domain.QShopImage;
import com.vinny.backend.Shop.domain.mapping.QShopVintageStyle;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.QRegion;
import com.vinny.backend.User.domain.QVintageStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShopRankingQueryRepositoryImpl implements ShopRankingQueryRepository {

    private final JPAQueryFactory query;

    private static final QShop s = QShop.shop;
    private static final QRegion r = QRegion.region;
    private static final QShopImage img = QShopImage.shopImage;
    private static final QShopVintageStyle svs = QShopVintageStyle.shopVintageStyle;
    private static final QVintageStyle vs = QVintageStyle.vintageStyle;

    @Override
    public Page<Shop> searchRankedByVisit(List<String> regionKeywords, List<String> styleNames, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();

        // 지역 필터
        if (regionKeywords != null && !regionKeywords.isEmpty()) {
            // region.name OR shop.address 에 여러 키워드 적용
            BooleanBuilder regionCond = new BooleanBuilder();
            for (String keyword : regionKeywords) {
                regionCond.or(r.name.contains(keyword))
                        .or(s.address.contains(keyword));
            }
            where.and(regionCond);
        }

        // 스타일 필터
        if (styleNames != null && !styleNames.isEmpty()) {
            where.and(
                    JPAExpressions
                            .selectOne()
                            .from(svs)
                            .join(svs.vintageStyle, vs)
                            .where(
                                    svs.shop.eq(s)
                                            .and(vs.name.in(styleNames))   // ✅ eq → in
                            )
                            .exists()
            );
        }

        // 1) ID만 조회
        List<Long> ids = query
                .select(s.id)
                .from(s)
                .leftJoin(s.region, r)
                .where(where)
                .orderBy(s.visitCount.coalesce(0).desc(), s.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 2) 총 개수
        Long total = query
                .select(s.id.countDistinct())
                .from(s)
                .leftJoin(s.region, r)
                .where(where)
                .fetchOne();

        // 3) 상세 로딩
        List<Shop> content = query
                .selectFrom(s)
                .leftJoin(s.region, r).fetchJoin()
                .leftJoin(s.shopVintageStyleList, svs).fetchJoin()
                .leftJoin(svs.vintageStyle, vs).fetchJoin()
                .where(s.id.in(ids))
                .distinct()
                .fetch();

        // 4) 원래 순서 유지
        content.sort((a, b) -> Integer.compare(ids.indexOf(a.getId()), ids.indexOf(b.getId())));

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

}
