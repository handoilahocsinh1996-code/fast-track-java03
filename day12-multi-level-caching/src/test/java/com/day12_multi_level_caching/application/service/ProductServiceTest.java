package com.day12_multi_level_caching.application.service;

import com.day12_multi_level_caching.application.port.out.CacheInvalidationPublisher;
import com.day12_multi_level_caching.application.port.out.ProductCache;
import com.day12_multi_level_caching.application.port.out.ProductRepository;
import com.day12_multi_level_caching.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductCache productCache;
    private CacheInvalidationPublisher cacheInvalidationPublisher;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productCache = mock(ProductCache.class);
        cacheInvalidationPublisher = mock(CacheInvalidationPublisher.class);

        productService = new ProductService(
                productRepository,
                productCache,
                cacheInvalidationPublisher);
    }

    @Test
    void shouldReturnProductFromCacheWhenCacheHit() {
        Product product = new Product(
                1L,
                "Keyboard",
                new BigDecimal("599000.00"));

        when(productCache.get(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Keyboard", result.get().name());

        verify(productRepository, never()).findById(1L);
    }

    @Test
    void shouldLoadProductFromRepositoryWhenCacheMiss() {
        Product product = new Product(
                1L,
                "Keyboard",
                new BigDecimal("599000.00"));

        when(productCache.get(1L)).thenReturn(Optional.empty());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Keyboard", result.get().name());

        verify(productRepository).findById(1L);
        verify(productCache).put(product);
    }

    @Test
    void shouldDeleteProductAndInvalidateCache() {
        when(productRepository.deleteById(1L)).thenReturn(true);

        boolean result = productService.deleteProductById(1L);

        assertTrue(result);

        verify(productCache).evict(1L);
        verify(cacheInvalidationPublisher).publishProductInvalidation(1L);
    }

    @Test
    void shouldNotInvalidateCacheWhenProductDoesNotExist() {
        when(productRepository.deleteById(1L)).thenReturn(false);

        boolean result = productService.deleteProductById(1L);

        assertFalse(result);

        verify(productCache, never()).evict(anyLong());
        verify(cacheInvalidationPublisher, never())
                .publishProductInvalidation(anyLong());
    }

    @Test
    void shouldRejectInvalidProductId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> productService.getProductById(0L));

        verifyNoInteractions(productCache, productRepository);
    }
}