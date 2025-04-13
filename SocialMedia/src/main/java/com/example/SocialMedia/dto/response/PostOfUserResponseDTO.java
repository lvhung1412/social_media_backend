package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostOfUserResponseDTO {
    private String id;
    private PostFatherResponseDTO post;
    private Date createDate;
    private String value;
    private String security;
    private String likedPost = "";
    private Integer countComment = 0;
    private List<Integer> countReaction ;
    private List<FileOfPostResponseDTO> files;
}
