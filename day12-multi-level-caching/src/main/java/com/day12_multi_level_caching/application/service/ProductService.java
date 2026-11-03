package com.day12_multi_level_caching.application.service;

import com.day12_multi_level_caching.application.port.in.CreateProductUseCase;
import com.day12_multi_level_caching.application.port.in.DeleteProductUseCase;
import com.day12_multi_level_caching.application.port.in.GetProductUseCase;
import com.day12_multi_level_caching.application.port.out.CacheInvalidationPublisher;
import com.day12_multi_level_caching.application.port.out.ProductCache;
import com.day12_multi_level_caching.application.port.out.ProductRepository;
import com.day12_multi_level_caching.domain.model.Product;

import java.math.BigDecimal;
import java.util.Optional;

public class ProductService implements
        CreateProductUseCase,
        GetProductUseCase,
        DeleteProductUseCase {

    private final ProductRepository productRepository;
    private final ProductCache productCache;
    private final CacheInvalidationPublisher cacheInvalidationPublisher;

    public ProductService(
            ProductRepository productRepository,
            ProductCache productCache,
            CacheInvalidationPublisher cacheInvalidationPublisher) {
        this.productRepository = productRepository;
        this.productCache = productCache;
        this.cacheInvalidationPublisher = cacheInvalidationPublisher;
    }

    @Override
    public Product createProduct(String name, BigDecimal price) {
        Product product = new Product(null, name, price);

        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        validateId(id);

        Optional<Product> cachedProduct = productCache.get(id);

        if (cachedProduct.isPresent()) {
            return cachedProduct;
        }

        Optional<Product> product = productRepository.findById(id);

        product.ifPresent(productCache::put);

        return product;
    }

    @Override
    public boolean deleteProductById(Long id) {
        validateId(id);

        boolean deleted = productRepository.deleteById(id);

        if (!deleted) {
            return false;
        }

        productCache.evict(id);
        cacheInvalidationPublisher.publishProductInvalidation(id);

        return true;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be positive");
        }
    }
}