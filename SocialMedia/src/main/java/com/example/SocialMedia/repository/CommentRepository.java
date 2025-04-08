package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Comment;
import com.example.SocialMedia.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByPostAndStatusAndCommentIsNull(Post post, String status, Sort sort);
    Page<Comment> findAllByPostAndStatusAndCommentIsNull(Post post, String status, Pageable pageable);

    List<Comment> findAllByCommentAndStatus(Comment comment, String status);
    Page<Comment> findAllByCommentAndStatus(Comment comment, String status, Pageable pageable);
}
