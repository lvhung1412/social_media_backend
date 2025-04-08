package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Post;
import com.example.SocialMedia.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByUserAndStatus(User user, String status, Sort sort);
    List<Post> findAllByUser(User user, Sort sort);

    Page<Post> findAllByStatus(String status, Pageable pageable);

    Page<Post> findAllByUserAndStatus(User user, String status, Pageable pageable );

    Optional<Post> findByIdAndStatus(String id, String status);
}
