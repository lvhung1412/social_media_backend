package com.example.SocialMedia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResponseDTO {
    private String username;
    private String name;
    private String avatar;
    private Integer countCommonFriend = 0;
}
