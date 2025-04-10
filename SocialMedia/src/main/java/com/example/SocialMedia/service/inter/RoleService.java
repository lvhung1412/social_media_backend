package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface RoleService {
    DataResponse<?> createStory(User user, MultipartFile[] files);
    ListResponse<?> getAllStoryByUser(User user);

    DataResponse<?> getStoryById(String id);
    ListResponse<?> getListStory(User user);
    DataResponse<?> deletetStory(String id);
}
