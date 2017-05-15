package com.example.sales.application.service;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.service.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.infrastructure.SalesIdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    SalesIdentifierFactory identifierFactory;


    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PlantNotFoundException {
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(purchaseOrderDTO.getPlant().get_id());
        BusinessPeriod rentalPeriod = BusinessPeriod.of(purchaseOrderDTO.getRentalPeriod().getStartDate(), purchaseOrderDTO.getRentalPeriod().getEndDate());

        PurchaseOrder po = PurchaseOrder.of(
                identifierFactory.nextPurchaseOrderID(),
                plantInventoryEntry,
                rentalPeriod);

//        DataBinder binder = new DataBinder(po);
//        binder.addValidators(new PurchaseOrderValidator(new BusinessPeriodValidator()));
//        binder.validate();
//
//        if (binder.getBindingResult().hasErrors())
//            throw new BindException(binder.getBindingResult());

        po = purchaseOrderRepository.save(po);
        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(plantInventoryEntry, rentalPeriod);
            po.confirmReservation(plantReservation, plantInventoryEntry.getPrice());
            po = purchaseOrderRepository.save(po);
            return purchaseOrderAssembler.toResource(po);
        } catch (PlantNotFoundException e) {
            po.handleRejection();
            purchaseOrderRepository.save(po);

            throw e;
        }
    }

    public PurchaseOrderDTO updatePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PlantNotFoundException {
        PurchaseOrder po = purchaseOrderRepository.findOne(purchaseOrderDTO.get_id());
        BusinessPeriod rentalPeriod = BusinessPeriod.of(purchaseOrderDTO.getRentalPeriod().getStartDate(), purchaseOrderDTO.getRentalPeriod().getEndDate());

        PurchaseOrder newPo = PurchaseOrder.of(
                po.getId(),
                po.getPlant(),
                rentalPeriod
        );

        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(newPo.getPlant(), rentalPeriod);
            newPo.confirmReservation(plantReservation, newPo.getPlant().getPrice());
            newPo = purchaseOrderRepository.save(newPo);
            return purchaseOrderAssembler.toResource(newPo);
        } catch (PlantNotFoundException e) {
            newPo.handleRejection();
            purchaseOrderRepository.save(newPo);

            throw e;
        }
    }

    public PurchaseOrderDTO findPurchaseOrder(String id) {
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.findOne(id));
    }

    public List<PurchaseOrderDTO> findAll() {
        return purchaseOrderAssembler.toResources(purchaseOrderRepository.findAll());
    }

    public PurchaseOrderDTO acceptPurchaseOrder(String id) {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        po.handleAcceptance();
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }
    public PurchaseOrderDTO rejectPurchaseOrder(String id) {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        po.handleRejection();
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }
    public PurchaseOrderDTO closePurchaseOrder(String id) throws PurchaseOrderException{
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        if (LocalDate.now().isBefore(po.getRentalPeriod().getStartDate())) {
            po.handleClosure();
        } else {
            throw new PurchaseOrderException("Too late to cancel purchase order.");
        }
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }
}
