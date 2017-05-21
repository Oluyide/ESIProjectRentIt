package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class PlantReservation {
    @Id
    String id;

    @ManyToOne
    PlantInventoryItem plant;

    @Embedded
    BusinessPeriod schedule;

    public void changeReservedPlant(PlantInventoryItem plant){
        this.plant = plant;
    }
}
