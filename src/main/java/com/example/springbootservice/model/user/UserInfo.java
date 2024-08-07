package com.example.springbootservice.model.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
