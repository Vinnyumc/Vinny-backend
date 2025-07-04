package com.vinny.backend.User.domain.mapping;

import com.vinny.backend.Common.domain.BaseEntity;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.VintageItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserVintageItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vintage_item_id", nullable = false)
    private VintageItem vintageItem;
}
