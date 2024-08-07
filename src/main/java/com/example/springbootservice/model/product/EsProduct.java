package com.example.springbootservice.model.product;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Document(indexName = "product")
public class EsProduct {
    @Id
    private String id;
    private String mongoId;
    private String name;
    private float price;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private LocalDate launchDate;
    private GeoPoint coordinatesOfOrigin;

    public static EsProduct fromProduct(Product product) {
        List<Double> coordinates = product.getCoordinatesOfOrigin().getCoordinates();
        return EsProduct.builder()
                .mongoId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .launchDate(product.getLaunchDate())
                .coordinatesOfOrigin(new GeoPoint(coordinates.get(1), coordinates.get(0)))
                .build();
    }
}
