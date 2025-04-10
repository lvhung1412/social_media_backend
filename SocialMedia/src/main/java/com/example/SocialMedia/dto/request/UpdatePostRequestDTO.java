package com.example.SocialMedia.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDTO {
    @NotNull(message = "Value is required")
    private String value;
    private String security;
    private List<String> deletedFile;
}
