package com.example.SocialMedia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoryResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private Date createDate;
    private String value;
    private String music;
    private String status;
}
