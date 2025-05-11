package com.multiservice.AppService.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String identifier; // could be email or username

    @NotBlank
    private String password;
}