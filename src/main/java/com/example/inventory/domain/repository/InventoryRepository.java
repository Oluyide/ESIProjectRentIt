package com.example.inventory.domain.repository;

import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lgarcia on 2/17/2017.
 */
@Repository
public interface InventoryRepository extends JpaRepository<PlantInventoryEntry, String>, CustomInventoryRepository {
    @Query("select i from PlantInventoryItem i where i.plantInfo = ?1 and i.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and i not in (select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)")
    List<PlantInventoryItem> findAvailableInventoryItems(PlantInventoryEntry plantInventoryEntry, LocalDate startDate, LocalDate endDate);

}
