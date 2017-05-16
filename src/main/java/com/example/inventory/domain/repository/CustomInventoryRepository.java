package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lgarcia on 2/17/2017.
 */
public interface CustomInventoryRepository {
    List<PlantInventoryEntry> findAvailable(String name, LocalDate startDate, LocalDate endDate);
    List<PlantInventoryEntry> findDeployedOn(LocalDate startDate);
}
