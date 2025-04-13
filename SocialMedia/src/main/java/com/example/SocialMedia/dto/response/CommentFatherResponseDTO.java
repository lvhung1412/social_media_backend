package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentFatherResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private String value;
    private Integer type;
    private Date createDate;
    private String status;
}
