package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostOfCommentResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private String value;
}
