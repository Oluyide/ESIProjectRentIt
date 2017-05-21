package com.example.inventory.application.service;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.integration.MailIntegration;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.*;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.inventory.infrastructure.InventoryIdentifierFactory;
import com.example.inventory.infrastructure.InvoiceIdentifierFactory;
import com.example.remittances.domain.model.Invoice;
import com.example.remittances.domain.model.InvoiceStatus;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.ExceptionListener;
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

    @Autowired
    InvoiceIdentifierFactory invoiceIdentifierFactory;

    @Autowired
    MailIntegration mailIntegration;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    public List<PlantInventoryEntryDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return plantInventoryEntryAssembler.toResources(inventoryRepository.findAvailable(name, startDate, endDate));
    }

    public List<PlantInventoryEntryDTO> findReservations(LocalDate startDate) {
        return plantInventoryEntryAssembler.toResources(inventoryRepository.findDeployedOn(startDate));
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

    public PlantInventoryItem handlePlantStatusChange(String id, PlantInventoryItemStatus status) throws PlantNotFoundException{
        PlantInventoryItem item = findPlantItem(id);
        item.handleStatusChange(status);

        return plantInventoryItemRepository.save(item);
    }

    public void generateInvoice(PlantInventoryItem item){
        Invoice invoice = Invoice.of(
                invoiceIdentifierFactory.nextInvoiceID(),
                item.getPlantInfo().getPrice(),
                InvoiceStatus.PENDING
        );


        String invoice1 =
                "{\n" +
                        "   \"_id\":\"" + invoice.get_id() + "\",\n" +
                        "   \"totalPrice\":" + invoice.getTotalPrice() + ",\n" +
                        "   \"invoiceStatus\": \"" + invoice.getInvoiceStatus() +"\"\n" +
                 "}\n";
        try {
            mailIntegration.sendMail(
                    "esi2017.g17@gmail.com",
                    "invoice",
                    "Hello\n\nYour invoice is attached.",
                    "invoice.json",
                    invoice1
            );
        }
        catch (Exception e){}

    }

    public PlantInventoryItem handleEquipmentConditionChange(String id, EquipmentCondition condition) throws PlantNotFoundException{
        PlantInventoryItem item = findPlantItem(id);
        item.handleConditionChange(condition);
        return plantInventoryItemRepository.save(item);
    }


    public PlantInventoryItem replaceRepairedPlant(PlantInventoryItem item) throws Exception{
        PlantInventoryEntry entry = item.getPlantInfo();
        List<PlantInventoryItem> items = plantInventoryItemRepository.findAllByPlantInfoAndEquipmentCondition(entry, EquipmentCondition.SERVICEABLE);

        PlantReservation currentReservation = plantReservationRepository.findPlantReservationByPlant(item);

        if (currentReservation == null)
            throw new Exception("Plant reservation not found");

        List<PlantInventoryItem> availableItems = inventoryRepository.findAvailableInventoryItems(
                entry,
                currentReservation.getSchedule().getStartDate(),
                currentReservation.getSchedule().getEndDate()
        );

        if (availableItems.size() == 0)
            throw new Exception("There are no available plants");

        PlantInventoryItem newItem = availableItems.get(0);

        currentReservation.changeReservedPlant(newItem);
        plantReservationRepository.save(currentReservation);

        return newItem;

        /*for (PlantInventoryItem i: items) {
            if (!i.getId().equalsIgnoreCase(item.getId()) && i.getEquipmentCondition() == EquipmentCondition.SERVICEABLE){

                System.out.println("has found a new item with id" + i.getId());

                i.handleStatusChange(PlantInventoryItemStatus.RESERVED);


                item.handleStatusChange(PlantInventoryItemStatus.RETURNED);
                plantInventoryItemRepository.save(item);

                return plantInventoryItemRepository.save(i);
            }
            break;
        }

        return item;*/
    }

    public PlantInventoryItem ScheduleMaintenance (String id) throws Exception
    {
        PlantInventoryItem item = findPlantItem(id);
        replaceRepairedPlant(item);
        handleEquipmentConditionChange(id, EquipmentCondition.UNSERVICEABLE_REPAIRABLE);

        return item;
    }

    public void CompleteMaintenance (String id) throws PlantNotFoundException
    {
        handleEquipmentConditionChange(id, EquipmentCondition.SERVICEABLE);
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
        PlantInventoryItem item = handlePlantStatusChange(id, PlantInventoryItemStatus.RETURNED);
        generateInvoice(item);
    }


}
