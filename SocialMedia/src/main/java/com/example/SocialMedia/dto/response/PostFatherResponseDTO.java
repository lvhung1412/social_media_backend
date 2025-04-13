package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostFatherResponseDTO {
    private String id;
    private SearchUserResponseDTO user;
    private Date createDate;
    private String value;
    private List<FileOfPostResponseDTO> files;
}
