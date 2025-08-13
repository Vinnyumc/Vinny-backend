package com.vinny.backend.Shop.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.vinny.backend.Shop.domain.QShop;
import com.vinny.backend.Shop.domain.enums.Status;
import com.vinny.backend.Shop.domain.mapping.QShopBrand;
import com.vinny.backend.Shop.domain.mapping.QShopVintageItem;
import com.vinny.backend.Shop.domain.mapping.QShopVintageStyle;
import com.vinny.backend.User.domain.mapping.QUserBrand;
import com.vinny.backend.User.domain.mapping.QUserRegion;
import com.vinny.backend.User.domain.mapping.QUserVintageItem;
import com.vinny.backend.User.domain.mapping.QUserVintageStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShopQueryRepositoryImpl implements ShopQueryRepository {

    private final JPAQueryFactory qf;

    // Q-types
    private final QShop s = QShop.shop;
    private final QUserRegion ur = QUserRegion.userRegion;

    private final QShopBrand sb = QShopBrand.shopBrand;
    private final QUserBrand ub = QUserBrand.userBrand;

    private final QShopVintageItem svi = QShopVintageItem.shopVintageItem;
    private final QUserVintageItem uvi = QUserVintageItem.userVintageItem;

    private final QShopVintageStyle svs = QShopVintageStyle.shopVintageStyle;
    private final QUserVintageStyle uvs = QUserVintageStyle.userVintageStyle;

    @Override
    public List<Long> findMatchedShopIdsRandomByUser(Long userId) {
        // RAND(seed) : MySQL/H2 둘 다 지원. id별로 결정적인 난수 → 중복 join에도 같은 값
        NumberExpression<Double> r = Expressions.numberTemplate(Double.class, "rand({0})", s.id);

        List<com.querydsl.core.Tuple> rows = qf
                .select(s.id, r)
                .from(s)
                // Region
                .join(ur).on(ur.user.id.eq(userId).and(s.region.id.eq(ur.region.id)))
                // Brand
                .join(sb).on(sb.shop.id.eq(s.id))
                .join(ub).on(ub.user.id.eq(userId).and(ub.brand.id.eq(sb.brand.id)))
                // Vintage Item
                 .join(svi).on(svi.shop.id.eq(s.id))
                 .join(uvi).on(uvi.user.id.eq(userId).and(uvi.vintageItem.id.eq(svi.vintageItem.id)))
                // Vintage Style
                 .join(svs).on(svs.shop.id.eq(s.id))
                 .join(uvs).on(uvs.user.id.eq(userId).and(uvs.vintageStyle.id.eq(svs.vintageStyle.id)))
                // 상태: OPEN
                .where(s.status.eq(Status.OPEN))
                .groupBy(s.id, r)
                .orderBy(r.asc())
                // .limit(원하는개수)  // 필요하면 제한
                .fetch();

        return rows.stream().map(t -> t.get(s.id)).toList();
    }

}
