package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, String> {
}
