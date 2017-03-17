package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by lgarcia on 2/10/2017.
 */
@Repository
public interface PlantInventoryEntryRepository extends JpaRepository<PlantInventoryEntry, String> {
}
