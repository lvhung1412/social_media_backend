package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.Notify;
import com.example.SocialMedia.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifyRepository extends JpaRepository<Notify, String> {
    List<Notify> findAllByUser(User user, Sort sort);
}
