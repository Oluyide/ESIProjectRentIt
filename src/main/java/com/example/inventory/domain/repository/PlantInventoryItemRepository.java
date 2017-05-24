package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantInventoryItemRepository extends JpaRepository<PlantInventoryItem, String>{
    PlantInventoryItem findOneByPlantInfo(PlantInventoryEntry entry);
    List<PlantInventoryItem> findAllByPlantInfoAndEquipmentCondition(PlantInventoryEntry entry, EquipmentCondition condition);
    List<PlantInventoryItem> findAllByPlantInfo(PlantInventoryEntry entry);
}
