package com.example.springbootservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username; // accept that username is unique

    @JsonIgnore
    @NotBlank
    private String password;

    @NotNull
    @Valid
    private UserInfo userInfo;
    private List<Permission> permissions;
}
