package com.day12_multi_level_caching.adapter.out.persistence;

import com.day12_multi_level_caching.application.port.out.ProductRepository;
import com.day12_multi_level_caching.domain.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductPersistenceAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    public ProductPersistenceAdapter(
            JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = new ProductEntity(
                product.name(),
                product.price());

        ProductEntity savedEntity = jpaProductRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public boolean deleteById(Long id) {
        if (!jpaProductRepository.existsById(id)) {
            return false;
        }

        jpaProductRepository.deleteById(id);
        return true;
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice());
    }
}