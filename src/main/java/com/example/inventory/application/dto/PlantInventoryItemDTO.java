package com.example.inventory.application.dto;

import com.example.common.rest.ResourceSupport;
import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItemStatus;
import lombok.Data;

/**
 * Created by gkgranada on 25/05/2017.
 */
@Data
public class PlantInventoryItemDTO extends ResourceSupport{
    String _id;
    String serialNumber;
    EquipmentCondition equipmentCondition;
    PlantInventoryEntryDTO plantInfo;
    PlantInventoryItemStatus plantStatus;
}
