package com.example.springbootservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserInfo {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
