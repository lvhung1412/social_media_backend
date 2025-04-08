package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Story;
import com.example.SocialMedia.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, String> {
    List<Story> findAllByUserAndStatus(User user, String status);

    Page<Story> findAllByUserAndStatus(User user, String status, Pageable pageable);
}
