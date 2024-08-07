package com.example.springbootservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateProductRequest {
    @NotBlank
    @Size(min = 2, max = 60)
    private String name;

    @Positive
    private float price;

    private LocalDate launchDate;
    private double longitude;
    private double latitude;
}
