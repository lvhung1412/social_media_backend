package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.request.*;
import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.dto.response.ListResponse;
import com.example.SocialMedia.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    DataResponse<?> saveUser(RegisterUserRequestDTO registerUserRequestDTO);

    DataResponse<?> updateUser(User user, UserUpdateRequestDTO userUpdateRequestDTO);

    DataResponse<?> changePassword(String username, ChangePasswordRequestDTO changePasswordRequestDTO);

    DataResponse<?> getUserById(String username, User user);

    DataResponse<?> verifyUser(VerifyRequestDTO verifyRequestDTO);

    DataResponse<?> sendVerifyCode(String email);

    DataResponse<?> changeAvatar(User user, MultipartFile avatar);

    DataResponse<?> changeEmail(User user, String email);

    DataResponse<?> forgotPassword(User user, ForgotPasswordRequestDTO forgotPasswordRequestDTO);

    ListResponse<?> searchUser(String name, User user);

    ListResponse<?> getRecommendFriend(User user);

    ListResponse<?> searchFriend(User user, String name);

    DataResponse<?> countFriend(User user);

    ListResponse<?> getListFriendWithPage(User user, Integer size);

    ListResponse<?> getListPost(User user);

    ListResponse<?> getListUserOfPage(Integer page);

    DataResponse<?> disableUser(String username);

    DataResponse<?> enableUser(String username);

    DataResponse<?> getStatisticData();

    ListResponse<?> uploadFileOfMessage(MultipartFile[] files);
}
