package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.request.NotifyRequestDTO;
import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.entity.User;

public interface NotifyService {
    ListResponse<?> getAllNotifyByUser(User user);

    DataResponse<?> createNotify(NotifyRequestDTO notifyRequestDTO);

    ListResponse<?> seenNotify(String[] ids);
}
