package com.vinny.backend.post.domain;

import com.vinny.backend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import com.vinny.backend.User.domain.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Like extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
