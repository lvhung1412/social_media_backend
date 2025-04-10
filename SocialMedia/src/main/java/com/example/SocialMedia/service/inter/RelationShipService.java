package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.entity.User;

public interface RelationShipService {
    ListResponse<?> getAllRelationShipByUsername(String username);

    ListResponse<?> getAllRelationShipByUser(User user);

    ListResponse<?> getAllInvitationFriend(User user);

    ListResponse<?> getAllFriend(User user);

    DataResponse<?> getRelationShipById(User user, String username );

    DataResponse<?> createRelationShip(User userFrom, String usernameTo);

    DataResponse<?> updateRelationShip(User userFrom, String usernameTo, String status);

    DataResponse<?> deleteRelationShip(User user, String friend);

}
