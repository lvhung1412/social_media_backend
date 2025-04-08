package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.RelationShip;
import com.example.SocialMedia.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationShipRepository extends JpaRepository<RelationShip, String> {
    List<RelationShip> findAllByUserFromOrUserToAndStatus(User userFrom, User userTo, String status);

    Page<RelationShip> findAllByUserFromOrUserToAndStatus(User userFrom, User userTo, String status, Pageable pageable);
    List<RelationShip> findAllByUserToAndStatus(User userTo, String status);
}
