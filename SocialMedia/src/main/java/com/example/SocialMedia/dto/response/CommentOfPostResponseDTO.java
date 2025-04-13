package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentOfPostResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private String value;
    private Integer type;
    private Date createDate;
    private String status;
    private Integer countRep = 0;
    private List<ReactionOfCommentResponseDTO> reactions;
}
