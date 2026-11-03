package com.inventory_concurrency.infrastructure.persistence;

import com.inventory_concurrency.application.dto.InventoryHistoryResponse;
import com.inventory_concurrency.application.port.out.InventoryHistoryPort;
import com.inventory_concurrency.domain.model.Inventory;

import jakarta.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.DefaultRevisionEntity;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Repository
public class EnversInventoryHistoryAdapter implements InventoryHistoryPort {

    private final EntityManager entityManager;

    public EnversInventoryHistoryAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> findHistoryByProductId(
            Long productId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(Inventory.class, false, true)
                .add(AuditEntity.id().eq(productId))
                .addOrder(AuditEntity.revisionNumber().asc())
                .getResultList();

        return revisions.stream()
                .map(this::toResponse)
                .toList();
    }

    private InventoryHistoryResponse toResponse(Object[] revision) {
        Inventory inventory = (Inventory) revision[0];

        DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) revision[1];

        RevisionType revisionType = (RevisionType) revision[2];

        return new InventoryHistoryResponse(
                revisionEntity.getId(), // int có thể tự chuyển thành Long
                Instant.ofEpochMilli(revisionEntity.getTimestamp()),
                inventory.getProductName(),
                inventory.getQuantity(),
                revisionType.name());
    }
}