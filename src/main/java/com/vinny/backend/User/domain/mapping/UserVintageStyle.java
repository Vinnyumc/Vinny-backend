package com.vinny.backend.User.domain.mapping;

import com.vinny.backend.common.domain.BaseEntity;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.VintageStyle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserVintageStyle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintage_style_id", nullable = false)
    private VintageStyle vintageStyle;
}