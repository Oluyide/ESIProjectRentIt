package com.example.sales.domain.repository;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, String> {
    public PurchaseOrder findByPlant(PlantInventoryEntry entry);
}
