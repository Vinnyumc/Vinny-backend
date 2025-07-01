package com.vinny.backend.post.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostImagesId implements Serializable {

    private Long postId;
    private Long sequence;

    public PostImagesId() {}

    public PostImagesId(Long postId, Long sequence) {
        this.postId = postId;
        this.sequence = sequence;
    }

    // getter, setter 생략 가능 (Lombok 사용 가능)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostImagesId)) return false;
        PostImagesId that = (PostImagesId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(sequence, that.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, sequence);
    }
}