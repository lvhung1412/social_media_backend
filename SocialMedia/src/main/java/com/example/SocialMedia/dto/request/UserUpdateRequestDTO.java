package com.example.SocialMedia.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDTO {
    private String name;
    @Size(max = 12, min = 9)
    private String phone;
    private Date birthday;
    private String gender;
    private String nickname;
    private String bio;
    private String security;
}
