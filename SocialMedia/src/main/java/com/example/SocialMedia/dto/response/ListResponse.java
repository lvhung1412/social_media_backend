package com.example.SocialMedia.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class ListResponse<T> extends BaseResponse {
    private List<T> data;

    public ListResponse(List<T> data) {
        super(true, "");
        this.data = data;
    }
}