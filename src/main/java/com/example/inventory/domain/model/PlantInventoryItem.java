package com.example.inventory.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryItem {
    @Id
    String id;
    String serialNumber;

    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;
    @ManyToOne
    PlantInventoryEntry plantInfo;

    @Enumerated(EnumType.STRING)
    PlantInventoryItemStatus plantStatus;

    public void handleStatusChange(PlantInventoryItemStatus status){
        plantStatus = status;
    }
    public void handleConditionChange(EquipmentCondition condition){
        equipmentCondition = condition;
    }

}
