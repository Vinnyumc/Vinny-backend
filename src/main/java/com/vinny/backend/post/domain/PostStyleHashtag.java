package com.vinny.backend.post.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_style_hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class PostStyleHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintage_style_id", nullable = false)
    private VintageStyle vintageStyle;
}