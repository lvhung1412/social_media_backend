package com.example.SocialMedia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private HttpStatus status;
    private String message;
    private Object data;

    public ResponseObject(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
