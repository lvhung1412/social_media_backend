package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileOfPostResponseDTO {

    private String id;
    private String value;
    private Integer type;
    private String status;
}
