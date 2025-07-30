package com.vinny.backend.post.domain.mapping;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.common.domain.BaseEntity;
import com.vinny.backend.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "user_post_bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserPostBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
