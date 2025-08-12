package com.vinny.backend.User.dto;

import lombok.Getter;

@Getter
public class UserProfileDto {
    private Long userId;
    private String nickname;
    private String comment;
    private int postCount;
    private int bookmarkCount;

    public UserProfileDto(Long userId, String nickname, String comment) {
        this.userId = userId;
        this.nickname = nickname;
        this.comment = comment;
    }

    public UserProfileDto(Long userId, String nickname, String comment, int postCount, int bookmarkCount) {
        this.userId = userId;
        this.nickname = nickname;
        this.comment = comment;
        this.postCount = postCount;
        this.bookmarkCount = bookmarkCount;
    }

}