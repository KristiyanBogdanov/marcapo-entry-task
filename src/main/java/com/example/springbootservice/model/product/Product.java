package com.example.springbootservice.model.product;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    @Indexed(name = "nameIndex")
    private String name;

    @Indexed(name = "priceIndex")
    private float price;

    private LocalDate launchDate;
    private GeoJsonPoint coordinatesOfOrigin;
}
