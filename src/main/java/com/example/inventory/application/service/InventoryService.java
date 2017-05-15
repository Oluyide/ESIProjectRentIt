package com.example.inventory.application.service;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantInventoryItemStatus;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.inventory.infrastructure.InventoryIdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    PlantReservationRepository plantReservationRepository;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Autowired
    InventoryIdentifierFactory identifierFactory;

    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    public List<PlantInventoryEntryDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return plantInventoryEntryAssembler.toResources(inventoryRepository.findAvailable(name, startDate, endDate));
    }

    public PlantReservation createPlantReservation(PlantInventoryEntry plantInventoryEntry, BusinessPeriod schedule) throws PlantNotFoundException {
        List<PlantInventoryItem> items = inventoryRepository.findAvailableInventoryItems(plantInventoryEntry, schedule.getStartDate(), schedule.getEndDate());
        if (items.size() == 0)
            throw new PlantNotFoundException("Requested plant is unavailable");

        PlantReservation plantReservation = PlantReservation.of(identifierFactory.nextPlantInventoryEntryID(), items.get(0), schedule);
        plantReservationRepository.save(plantReservation);
        return plantReservation;
    }

    public PlantInventoryEntryDTO findPlant(String id) {
        return plantInventoryEntryAssembler.toResource(inventoryRepository.findOne(id));
    }

    public PlantInventoryItem findPlantItem(String id) throws PlantNotFoundException{

        PlantInventoryItem item = plantInventoryItemRepository.findOne(id);

        if (item == null){
            throw new PlantNotFoundException("Plant with provided ID doesn't exist");
        }

        return item;
    }

    public void handlePlantStatusChange(String id, PlantInventoryItemStatus status) throws PlantNotFoundException{
        PlantInventoryItem item = findPlantItem(id);
        item.handleStatusChange(status);
        plantInventoryItemRepository.save(item);
    }

    public void dispatchPlant(String id) throws PlantNotFoundException{
        handlePlantStatusChange(id, PlantInventoryItemStatus.DISPATCHED);
    }

    public void deliverPlant(String id) throws PlantNotFoundException{
        handlePlantStatusChange(id, PlantInventoryItemStatus.DELIVERED);
    }

    public void rejectPlant(String id) throws PlantNotFoundException{
        handlePlantStatusChange(id, PlantInventoryItemStatus.REJECTED_BY_CUSTOMER);
    }

    public void returnPlant(String id) throws PlantNotFoundException{
        handlePlantStatusChange(id, PlantInventoryItemStatus.RETURNED);
    }


}
