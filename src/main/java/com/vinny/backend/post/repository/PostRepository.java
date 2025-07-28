package com.vinny.backend.post.repository;

import com.vinny.backend.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT DISTINCT p FROM Post p
        LEFT JOIN p.user u
        LEFT JOIN p.brandHashtags bh ON bh.post = p
        LEFT JOIN bh.brand b
        LEFT JOIN p.shopHashtags sh ON sh.post = p
        LEFT JOIN sh.shop s
        LEFT JOIN p.styleHashtags st ON st.post = p
        LEFT JOIN st.vintageStyle vs
        WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(vs.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Post> searchByKeyword(@org.springframework.data.repository.query.Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.user u " +
            "LEFT JOIN FETCH p.images i",
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<Post> findAllWithAssociations(Pageable pageable);

    @Query("""
        SELECT DISTINCT p FROM Post p 
        JOIN p.styleHashtags sh 
        JOIN sh.vintageStyle vs 
        WHERE vs.name = :styleName
        """)
    Page<Post> findByStyle(@Param("styleName") String styleType, Pageable pageable);

    @Query("""
        SELECT DISTINCT p FROM Post p 
        JOIN p.styleHashtags sh 
        JOIN sh.vintageStyle vs 
        WHERE vs.name = :styleName 
        AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)
        """)
    Page<Post> findByStyleAndKeyword(
            @Param("styleName") String styleType,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user u
        LEFT JOIN FETCH p.images i
        LEFT JOIN FETCH p.brandHashtags bh
        LEFT JOIN FETCH bh.brand b
        LEFT JOIN FETCH p.shopHashtags sh
        LEFT JOIN FETCH sh.shop s
        LEFT JOIN FETCH p.styleHashtags sth
        LEFT JOIN FETCH sth.vintageStyle vs
        WHERE p.id = :postId
    """)
    Optional<Post> findByIdWithAllRelations(@Param("postId") Long postId);
}
