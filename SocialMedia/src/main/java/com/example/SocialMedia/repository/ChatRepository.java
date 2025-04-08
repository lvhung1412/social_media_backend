package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, String> {
}
