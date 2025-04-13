package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private PostOfCommentResponseDTO post;
    private CommentFatherResponseDTO comment;
    private String value;
    private Integer type;
    private Date createDate;
    private String status;
    private List<ReactionOfCommentResponseDTO> reactions;
}
