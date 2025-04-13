package com.example.SocialMedia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private PostFatherResponseDTO post;
    private Date createDate;
    private String value;
    private String status;
    private String security;
    private String likedPost = "";
    private Integer countComment = 0;
    private List<Integer> countReaction ;
    private List<FileOfPostResponseDTO> files;
}
