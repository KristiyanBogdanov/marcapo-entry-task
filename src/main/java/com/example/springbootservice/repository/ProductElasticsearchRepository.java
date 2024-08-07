package com.example.springbootservice.repository;

import com.example.springbootservice.model.product.EsProduct;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<EsProduct, String> {
    List<EsProduct> findByName(String name);
    List<EsProduct> findByPriceBetween(float startPrice, float endPrice);
    List<EsProduct> findByLaunchDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("{\"bool\": {\"filter\": {\"geo_distance\": {\"distance\": \"?2km\", \"coordinatesOfOrigin\": {\"lat\": ?0, \"lon\": ?1}}}}}")
    List<EsProduct> findByCoordinatesOfOriginWithin(double latitude, double longitude, double distanceKm);

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"*\"]} }")
    List<EsProduct> searchByAllFields(String query);
}
