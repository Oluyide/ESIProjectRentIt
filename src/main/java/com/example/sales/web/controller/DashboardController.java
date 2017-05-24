package com.example.sales.web.controller;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.service.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.service.SalesService;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.web.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    SalesService salesService;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }

    @PostMapping("/catalog/query")
    public String queryPlantCatalog(Model model, CatalogQueryDTO query) {
        List<PlantInventoryEntryDTO> plants = inventoryService.findAvailablePlants(
                query.getName(),
                query.getRentalPeriod().getStartDate(),
                query.getRentalPeriod().getEndDate()
        );
        model.addAttribute("plants", plants);
        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setRentalPeriod(query.getRentalPeriod());
        model.addAttribute("po", po);
        return "dashboard/catalog/query-result";
    }

    @GetMapping("/catalog/query/{id}")
    public String querySpecificPlant(Model model, @PathVariable String id){
        PlantInventoryEntryDTO entry = inventoryService.findPlant(id);
        model.addAttribute("plant", entry);

        return "dashboard/catalog/query-specific";
    }

    @GetMapping("/catalog/reservationsForm")
    public String getReservationsForm(Model model) {
        return "dashboard/catalog/reservations-form";
    }

    @GetMapping("/catalog/reservations")
    public String queryReservations(Model model, @RequestParam(name = "startDate")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate) {
        if(startDate.isPresent()) {
            List<PlantInventoryEntryDTO> plants = inventoryService.findReservations(startDate.get());
            model.addAttribute("plants", plants);
            model.addAttribute("startDate", startDate.get());
            return "dashboard/catalog/reservations-result";
        }
        else {
            throw new IllegalArgumentException("Start Date not present or wrong");
        }
    }

    @PostMapping("/orders")
    public String createPurchaseOrder(Model model, PurchaseOrderDTO purchaseOrderDTO) throws PlantNotFoundException {
        purchaseOrderDTO = salesService.createPurchaseOrder(purchaseOrderDTO);
        model.addAttribute("po", purchaseOrderDTO);
        return "redirect:/dashboard/orders/" + purchaseOrderDTO.get_id();
    }

    @GetMapping("/orders/{id}")
    public String showPurchaseOrder(Model model, @PathVariable String id) {
        PurchaseOrderDTO po = salesService.findPurchaseOrder(id);
        model.addAttribute("po", po);
        return "dashboard/orders/show";
    }

    @PostMapping("/orders/cancel")
    public String cancelPO(Model model, String id){

        String message = "";
        try{
            salesService.closePurchaseOrder(id);
            message = "Purchase order cancelled successfully";
        }
        catch(Exception e){
            message = e.getMessage();
        }


        model.addAttribute("message", message);

        return "dashboard/orders/cancel";
    }
}
