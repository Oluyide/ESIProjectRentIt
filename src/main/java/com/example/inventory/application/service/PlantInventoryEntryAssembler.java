package com.example.inventory.application.service;

import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.rest.controller.InventoryRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {
    public PlantInventoryEntryAssembler() {
        super(InventoryRestController.class, PlantInventoryEntryDTO.class);
    }
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plantInventoryEntry) {
        PlantInventoryEntryDTO dto = createResourceWithId(plantInventoryEntry.getId(), plantInventoryEntry);
        dto.set_id(plantInventoryEntry.getId());
        dto.setName(plantInventoryEntry.getName());
        dto.setDescription(plantInventoryEntry.getDescription());
        dto.setPrice(plantInventoryEntry.getPrice());
        return dto;
    }
}
