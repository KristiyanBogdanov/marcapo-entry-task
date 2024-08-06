package com.example.springbootservice.model.product;

import com.mongodb.client.model.geojson.Point;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @NotBlank
    @Size(min = 2, max = 60)
    private String name;

    @Positive
    private float price;

    private LocalDate launchDate;
    private Point coordinatesOfOrigin;
}
