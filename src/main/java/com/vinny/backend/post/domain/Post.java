package com.vinny.backend.post.domain;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.Brand;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.VintageStyle;
import com.vinny.backend.common.domain.BaseEntity;
import com.vinny.backend.post.domain.mapping.PostBrandHashtag;
import com.vinny.backend.post.domain.mapping.PostShopHashtag;
import com.vinny.backend.post.domain.mapping.PostStyleHashtag;
import com.vinny.backend.post.domain.mapping.UserPostLike;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 연관관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private List<UserPostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private Set<PostBrandHashtag> brandHashtags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default
    private Set<PostStyleHashtag> styleHashtags = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    @Builder.Default

    private Set<PostShopHashtag> shopHashtags = new HashSet<>();

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }

    public void clearBrandHashtags() { this.brandHashtags.clear(); }
    public void addBrandHashtag(Brand brand) {
        this.brandHashtags.add(new PostBrandHashtag(null, this, brand));
    }

    public void clearStyleHashtags() { this.styleHashtags.clear(); }
    public void addStyleHashtag(VintageStyle style) {
        this.styleHashtags.add(new PostStyleHashtag(null, this, style));
    }

    public void clearShopHashtags() { this.shopHashtags.clear(); }
    public void addShopHashtag(Shop shop) {
        this.shopHashtags.add(new PostShopHashtag(null, this, shop));
    }
    public List<PostImage> getPostImages() {
        return images;
    }
}
