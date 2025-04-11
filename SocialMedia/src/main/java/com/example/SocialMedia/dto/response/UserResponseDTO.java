package com.example.SocialMedia.dto.response;

import com.example.SocialMedia.entity.Address;
import com.example.SocialMedia.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private String name;
    private String phone;
    private String email;
    private Address address;
    private Date birthday;
    private String gender;
    private String nickname;
    private String bio;
    private Boolean enable;
    private String avatar;
    private Role role;
    private String security;
    private Integer countFriend = 0;
    private Integer countCommonFriend = 0;
}
