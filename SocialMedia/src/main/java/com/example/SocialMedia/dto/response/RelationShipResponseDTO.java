package com.example.SocialMedia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RelationShipResponseDTO {
    private SearchUserResponseDTO userFrom;
    private SearchUserResponseDTO userTo;
    private Date createDate;
    private String status;
}
