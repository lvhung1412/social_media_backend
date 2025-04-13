package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FriendResponseDTO {
    private SearchUserResponseDTO userFrom;
    private SearchUserResponseDTO userTo;
    private Date createDate;
    private String status;
}
