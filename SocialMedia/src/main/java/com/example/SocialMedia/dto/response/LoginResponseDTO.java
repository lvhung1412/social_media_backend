package com.example.SocialMedia.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO<T> {
    private String token;
    private final String type = "Bearer";
    private T userInfo;

    public LoginResponseDTO(String token, T userInfo) {

        this.token = token;
        this.userInfo = userInfo;
    }
}
