package com.example.sales.web.controller;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.service.InventoryService;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.service.SalesService;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.web.dto.CatalogQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        model.addAttribute("plants", plants
                );
        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setRentalPeriod(query.getRentalPeriod());
        model.addAttribute("po", po);
        return "dashboard/catalog/query-result";
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
}
