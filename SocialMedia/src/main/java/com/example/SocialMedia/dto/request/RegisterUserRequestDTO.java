package com.example.SocialMedia.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDTO {
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull(message="An email is required!")
    @Size(message="Invalid size.", max = 30, min=10)
    @Schema(type = "string", format = "email")
    private String email;
    @NotNull
    @Size(message="Invalid size.", max = 10, min=10)
    @Pattern(regexp=("^0\\d{9}$"), message = "Invalid phone")
    private String phone;
}
