package com.example.SocialMedia.repository;

import com.example.SocialMedia.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
