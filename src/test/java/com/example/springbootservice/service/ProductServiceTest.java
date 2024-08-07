package com.example.springbootservice.service;

import com.example.springbootservice.dto.CreateProductRequest;
import com.example.springbootservice.exception.EntityNotFoundException;
import com.example.springbootservice.model.product.EsProduct;
import com.example.springbootservice.model.product.Product;
import com.example.springbootservice.repository.ProductElasticsearchRepository;
import com.example.springbootservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductElasticsearchRepository productElasticsearchRepository;

    @InjectMocks
    private ProductService productService;

    private Product createdProduct;

    @BeforeEach
    void setUp() {
        createdProduct = Product.builder()
                .id("testMongoId")
                .name("TestProduct")
                .price(100.0f)
                .launchDate(LocalDate.now())
                .coordinatesOfOrigin(new GeoJsonPoint(45.0, 90.0))
                .build();
    }

    @Test
    void generateNProducts_ShouldSaveProductsInBothRepositories() {
        productService.generateNProducts(5);

        verify(productRepository, times(1)).saveAll(any());
        verify(productElasticsearchRepository, times(1)).saveAll(any());
    }

    @Test
    void create_ShouldSaveProductInBothRepositories() {
        CreateProductRequest productData = CreateProductRequest.builder()
                .name(createdProduct.getName())
                .price(createdProduct.getPrice())
                .launchDate(createdProduct.getLaunchDate())
                .longitude(createdProduct.getCoordinatesOfOrigin().getX())
                .latitude(createdProduct.getCoordinatesOfOrigin().getY())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(createdProduct);

        Product result = productService.create(productData);

        verify(productRepository, times(1)).save(any(Product.class));
        verify(productElasticsearchRepository, times(1)).save(any(EsProduct.class));

        assertEquals(createdProduct, result);
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        when(productRepository.findById(createdProduct.getId())).thenReturn(Optional.of(createdProduct));

        Product result = productService.findById(createdProduct.getId());

        verify(productRepository, times(1)).findById(createdProduct.getId());
        assertEquals(createdProduct, result);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        String productId = "nonExistentId";

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.findById(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void searchByName_ShouldReturnProducts_WhenExists() {
        List<EsProduct> mockedProducts = List.of(mock(EsProduct.class));

        when(productElasticsearchRepository.findByName(createdProduct.getName())).thenReturn(mockedProducts);

        List<EsProduct> result = productService.searchByName(createdProduct.getName());

        verify(productElasticsearchRepository, times(1)).findByName(createdProduct.getName());
        assertEquals(mockedProducts, result);
    }

    @Test
    void searchByPriceRange_ShouldReturnProducts_WhenExists() {
        float startPrice = 10.0f;
        float endPrice = 250.0f;
        List<EsProduct> mockedProducts = List.of(mock(EsProduct.class));

        when(productElasticsearchRepository.findByPriceBetween(startPrice, endPrice)).thenReturn(mockedProducts);

        List<EsProduct> result = productService.searchByPriceRange(startPrice, endPrice);

        verify(productElasticsearchRepository, times(1)).findByPriceBetween(startPrice, endPrice);
        assertEquals(mockedProducts, result);
    }

    @Test
    void searchWithin_ShouldReturnProducts_WhenExists() {
        double latitude = 45.0;
        double longitude = 90.0;
        double distanceKm = 100.0;
        List<EsProduct> mockedProducts = List.of(mock(EsProduct.class));

        when(productElasticsearchRepository.findByCoordinatesOfOriginWithin(latitude, longitude, distanceKm)).thenReturn(mockedProducts);

        List<EsProduct> result = productService.searchWithin(latitude, longitude, distanceKm);

        verify(productElasticsearchRepository, times(1)).findByCoordinatesOfOriginWithin(latitude, longitude, distanceKm);
        assertEquals(mockedProducts, result);
    }

    @Test
    void searchByAllFields_ShouldReturnProducts_WhenExists() {
        String query = "TestQuery";
        List<EsProduct> mockedProducts = List.of(mock(EsProduct.class));

        when(productElasticsearchRepository.searchByAllFields(query)).thenReturn(mockedProducts);

        List<EsProduct> result = productService.searchByAllFields(query);

        verify(productElasticsearchRepository, times(1)).searchByAllFields(query);
        assertEquals(mockedProducts, result);
    }
}
