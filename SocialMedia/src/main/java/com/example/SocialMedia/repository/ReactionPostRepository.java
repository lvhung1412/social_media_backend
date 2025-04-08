package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Post;
import com.example.SocialMedia.entity.ReactionPost;
import com.example.SocialMedia.entity.keys.ReactionPostKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionPostRepository extends JpaRepository<ReactionPost, ReactionPostKey> {
    List<ReactionPost> findAllByPostAndStatus(Post post, String status);

    Optional<ReactionPost> findById(ReactionPostKey reactionPostKey);

    Page<ReactionPost> findAllByPostAndStatus(Post post, String status, Pageable pageable);
}
