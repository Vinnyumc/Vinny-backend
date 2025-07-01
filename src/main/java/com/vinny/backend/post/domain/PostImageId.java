package com.vinny.backend.post.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostImageId implements Serializable {

    private Long postId;
    private Long sequence;

    public PostImageId() {}

    public PostImageId(Long postId, Long sequence) {
        this.postId = postId;
        this.sequence = sequence;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostImageId)) return false;
        PostImageId that = (PostImageId) o;
        return Objects.equals(postId, that.postId) &&
                Objects.equals(sequence, that.sequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, sequence);
    }
}