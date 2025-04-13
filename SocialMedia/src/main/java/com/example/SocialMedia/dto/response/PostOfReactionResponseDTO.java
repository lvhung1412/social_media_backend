package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PostOfReactionResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
}
