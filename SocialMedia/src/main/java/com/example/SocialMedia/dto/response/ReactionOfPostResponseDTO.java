package com.example.SocialMedia.dto.response;

import com.example.SocialMedia.entity.Reaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ReactionOfPostResponseDTO {
    private Reaction reaction;
    private SearchUserResponseDTO user;
    private Date createDate;
    private String status;
}
