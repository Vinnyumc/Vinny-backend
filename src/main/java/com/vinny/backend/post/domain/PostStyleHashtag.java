package com.vinny.backend.post.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "post_style_hashtag")
public class PostStyleHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintage_item_id", nullable = false)
    private VintageItem vintageItem;
}