package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.ReactionComment;
import com.example.SocialMedia.entity.keys.ReactionCommentKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionCommentRepository extends JpaRepository<ReactionComment, ReactionCommentKey> {
}
