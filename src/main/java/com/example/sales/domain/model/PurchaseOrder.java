package com.example.sales.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PurchaseOrder {
    @Id
    String id;

    @ManyToOne
    PlantInventoryEntry plant;
    @Embedded
    BusinessPeriod rentalPeriod;

    @OneToMany
    List<PlantReservation> plantReservations = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Column(precision = 8, scale = 2)
    BigDecimal total;

    public static PurchaseOrder of(String id, PlantInventoryEntry plant, BusinessPeriod rentalPeriod) {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.plant = plant;
        po.rentalPeriod = rentalPeriod;
        po.status = POStatus.CREATED;
        return po;
    }

    public void confirmReservation(PlantReservation plantReservation, BigDecimal price) {
        plantReservations.add(plantReservation);
        total = price.multiply(BigDecimal.valueOf(rentalPeriod.numberOfWorkingDays()));
        status = POStatus.PENDING;
    }

    public void handleRejection() {
        status = POStatus.REJECTED;
    }

    public void handleClosure(){
        status = POStatus.CLOSED;
    }

    public void handleAcceptance() {
        status = POStatus.OPEN;
    }
}
