package com.example.SocialMedia.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyRequestDTO {
    @NotNull
    String email;
    @NotNull
    String code;
}
