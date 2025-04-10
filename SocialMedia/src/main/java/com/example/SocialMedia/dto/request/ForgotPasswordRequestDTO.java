package com.example.SocialMedia.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ForgotPasswordRequestDTO {
    @NotNull
    private String newPassword;
    @NotNull
    private String verifyCode;
}
