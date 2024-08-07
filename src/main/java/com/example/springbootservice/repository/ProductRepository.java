package com.example.springbootservice.repository;

import com.example.springbootservice.model.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByName(String name);
    List<Product> findByPriceBetween(float startPrice, float endPrice);
    List<Product> findByLaunchDateBetween(LocalDate startDate, LocalDate endDate);
}
