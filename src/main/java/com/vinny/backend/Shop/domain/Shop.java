package com.vinny.backend.Shop.domain;

import com.vinny.backend.Shop.domain.mapping.ShopVintageStyle;
import com.vinny.backend.User.domain.Region;
import com.vinny.backend.common.domain.BaseEntity;
import com.vinny.backend.Shop.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "intro", nullable = false, length = 100)
    private String intro;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "instagram", length = 100)
    private String instagram;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopVintageStyle> shopVintageStyleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopImage> shopImages = new ArrayList<>();


    // Shop.java
    public void increaseVisitCount() {
        if (this.visitCount == null) this.visitCount = 0;
        this.visitCount = this.visitCount + 1;
    }

}
