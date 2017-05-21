package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantReservationRepository extends JpaRepository<PlantReservation, String>{
    public PlantReservation findPlantReservationByPlant(PlantInventoryItem item);
}
