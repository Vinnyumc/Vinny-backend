package com.vinny.backend.post.domain.mapping;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.common.domain.BaseEntity;
import com.vinny.backend.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_post_like") //like는 예약어라서 변경
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserPostLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
