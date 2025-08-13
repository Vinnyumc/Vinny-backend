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
    public Page<Shop> searchRankedByVisit(String regionKeyword, String styleName, Pageable pageable) {
        // 동적 조건
        BooleanBuilder where = new BooleanBuilder();
        if (regionKeyword != null && !regionKeyword.isBlank()) {
            where.and(r.name.contains(regionKeyword).or(s.address.contains(regionKeyword)));
        }
        if (styleName != null && !styleName.isBlank()) {
            where.and(
                    JPAExpressions
                            .selectOne()
                            .from(svs)
                            .join(svs.vintageStyle, vs)
                            .where(svs.shop.eq(s).and(vs.name.eq(styleName)))
                            .exists()
            );
        }

        // 1) ID 페이지 조회 (컬렉션 fetch-join과 페이지네이션 충돌 방지)
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

        // 3) 상세 로딩 (연관 fetch) + distinct로 중복 제거
        List<Shop> content = query
                .selectFrom(s)
                .leftJoin(s.region, r).fetchJoin()
                .leftJoin(s.shopVintageStyleList, svs).fetchJoin()
                .leftJoin(svs.vintageStyle, vs).fetchJoin()
                .where(s.id.in(ids))
                .distinct()
                .fetch();

        // 4) ID 순서대로 정렬 (단순 비교, 별도 Map 사용 안 함)
        content.sort((a, b) -> Integer.compare(ids.indexOf(a.getId()), ids.indexOf(b.getId())));

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
