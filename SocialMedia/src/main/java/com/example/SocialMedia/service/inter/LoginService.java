package com.example.SocialMedia.service.inter;

import com.example.SocialMedia.dto.response.DataResponse;
import com.example.SocialMedia.entity.enums.RoleEnum;

import javax.security.auth.login.LoginException;

public interface LoginService {
    DataResponse<?> authenticateWithUsernamePassword(String username, String password, RoleEnum requestedRole) throws LoginException;
}
