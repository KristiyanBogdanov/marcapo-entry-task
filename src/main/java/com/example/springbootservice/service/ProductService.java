package com.example.springbootservice.service;

import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import com.example.springbootservice.exception.EntityNotFoundException;
import com.example.springbootservice.model.product.EsProduct;
import com.example.springbootservice.model.product.Product;
import com.example.springbootservice.repository.ProductElasticsearchRepository;
import com.example.springbootservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Random randomGenerator = new Random();
    private final ProductRepository productRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final ElasticsearchOperations operations;

    private String generateRandomName(int index) {
        return "Product-" + index;
    }

    private float generateRandomPrice() {
        return 10 + (1000 - 10) * randomGenerator.nextFloat();
    }

    private LocalDate generateRandomLaunchDate() {
        long minDay = LocalDate.of(2000, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2024, 3, 31).toEpochDay();
        long randomDay = minDay + randomGenerator.nextLong(maxDay - minDay + 1);
        return LocalDate.ofEpochDay(randomDay);
    }

    private GeoJsonPoint generateRandomCoordinates() {
        double longitude = -180 + randomGenerator.nextFloat() * 360;
        double latitude = -90 + randomGenerator.nextDouble() * 180;
        return new GeoJsonPoint(longitude, latitude);
    }

    private Product initProductWithRandomValues(int index) {
        return Product.builder()
                .name(generateRandomName(index))
                .price(generateRandomPrice())
                .launchDate(generateRandomLaunchDate())
                .coordinatesOfOrigin(generateRandomCoordinates())
                .build();
    }

    @Transactional
    public void generateNProducts(int numberOfProducts) {
        for (int i = 0; i < numberOfProducts; i++) {
            Product product = initProductWithRandomValues(i);
            saveProduct(product);
        }
    }

    @Transactional
    public void saveProduct(Product product) {
        Product createdProduct = productRepository.save(product);
        productElasticsearchRepository.save(EsProduct.fromProduct(createdProduct));
    }

    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<EsProduct> searchByName(String name) {
        return productElasticsearchRepository.findByName(name);
    }

    public List<EsProduct> searchByPriceRange(float startPrice, float endPrice) {
        return productElasticsearchRepository.findByPriceBetween(startPrice, endPrice);
    }

    public List<EsProduct> searchByLaunchDateRange(String startDate, String endDate) {
        return productElasticsearchRepository.findByLaunchDateBetween(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    public List<EsProduct> searchWithin(double latitude, double longitude, double distanceKm) {
        return productElasticsearchRepository.findByCoordinatesOfOriginWithin(latitude, longitude, distanceKm);

//         Another way to implement the searchWithin method:
//                GeoDistanceQuery geoDistanceQuery = GeoDistanceQuery.of(g -> g
//                .field("coordinatesOfOrigin")
//                .distance(distanceKm + "km")
//                .location(l -> l.coords(List.of(longitude, latitude)))
//        );
//
//        NativeQuery query = new NativeQueryBuilder()
//                .withQuery(Query.of(q -> q.geoDistance(geoDistanceQuery)))
//                .build();
//
//        SearchHits<EsProduct> searchHits = operations.search(query, EsProduct.class);
//        return searchHits.getSearchHits().stream()
//                .map(SearchHit::getContent)
//                .collect(Collectors.toList());
    }

    public List<EsProduct> searchByAllFields(String query) {
        return productElasticsearchRepository.searchByAllFields(query);
    }
}
