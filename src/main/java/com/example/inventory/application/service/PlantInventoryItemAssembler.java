package com.example.inventory.application.service;

import com.example.inventory.application.dto.PlantInventoryItemDTO;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.inventory.rest.controller.InventoryRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 25/05/2017.
 */
@Service
public class PlantInventoryItemAssembler extends ResourceAssemblerSupport<PlantInventoryItem, PlantInventoryItemDTO> {

    @Autowired
    PlantInventoryItemRepository repository;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PlantInventoryItemAssembler() {
        super(InventoryRestController.class, PlantInventoryItemDTO.class);
    }
    public PlantInventoryItemDTO toResource(PlantInventoryItem plantInventoryItem) {
        PlantInventoryItemDTO dto = createResourceWithId(plantInventoryItem.getId(), plantInventoryItem);
        dto.set_id(plantInventoryItem.getId());
        dto.setEquipmentCondition(plantInventoryItem.getEquipmentCondition());
        dto.setPlantInfo(plantInventoryEntryAssembler.toResource(plantInventoryItem.getPlantInfo()));
        dto.setPlantStatus(plantInventoryItem.getPlantStatus());
        dto.setSerialNumber(plantInventoryItem.getSerialNumber());
        return dto;
    }

    public  PlantInventoryItem toResource(PlantInventoryItemDTO dto){
        PlantInventoryItem entry = PlantInventoryItem.of(
                dto.get_id(),
                dto.getSerialNumber(),
                dto.getEquipmentCondition(),
                plantInventoryEntryAssembler.toResource(dto.getPlantInfo()),
                dto.getPlantStatus());
        return entry;

    }
}
