package com.example.springbootservice.controller;

import com.example.springbootservice.model.product.EsProduct;
import com.example.springbootservice.model.product.Product;
import com.example.springbootservice.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).GENERATE_PRODUCTS.name())")
    @GetMapping("/generate")
    public ResponseEntity<Void> generateNProducts(@RequestParam("numberOfProducts") @Positive int numberOfProducts) {
        productService.generateNProducts(numberOfProducts);
        return ResponseEntity.noContent().build();
    }

//    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).CREATE_PRODUCT.name())")
//    @PostMapping
//    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
//        return ResponseEntity.ok(productService.save(product));
//    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).READ_PRODUCT.name())")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).SEARCH_PRODUCTS_BY_NAME.name())")
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<EsProduct>> searchByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).SEARCH_PRODUCTS_BY_PRICE.name())")
    @GetMapping("/search/price")
    public ResponseEntity<List<EsProduct>> searchByPrice(
            @RequestParam("startPrice") @Positive float startPrice,
            @RequestParam("endPrice") @Positive float endPrice
    ) {
        return ResponseEntity.ok(productService.searchByPriceRange(startPrice, endPrice));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).SEARCH_PRODUCTS_BY_LAUNCH_DATE.name())")
    @GetMapping("/search/launch-date")
    public ResponseEntity<List<EsProduct>> searchByLaunchDate(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        return ResponseEntity.ok(productService.searchByLaunchDateRange(startDate, endDate));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).SEARCH_PRODUCTS_BY_COORDINATES.name())")
    @GetMapping("/search/within")
    public ResponseEntity<List<EsProduct>> searchWithin(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("distanceKm") @Positive double distanceKm
    ) {
        return ResponseEntity.ok(productService.searchWithin(latitude, longitude, distanceKm));
    }

    @PreAuthorize("hasAuthority(T(com.example.springbootservice.model.user.Permission).SEARCH_PRODUCTS_BY_ALL_FIELDS.name())")
    @GetMapping("/search/general")
    public ResponseEntity<List<EsProduct>> searchByAllFields(@RequestParam("query") String query) {
        return ResponseEntity.ok(productService.searchByAllFields(query));
    }
}
