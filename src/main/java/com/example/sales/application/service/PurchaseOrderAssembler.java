package com.example.sales.application.service;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.rest.ExtendedLink;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.service.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.rest.controller.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderAssembler() {
        super(SalesRestController.class, PurchaseOrderDTO.class);
    }

    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = createResourceWithId(purchaseOrder.getId(), purchaseOrder);
        dto.set_id(purchaseOrder.getId());
        dto.setPlant(plantInventoryEntryAssembler.toResource(purchaseOrder.getPlant()));
        dto.setRentalPeriod(BusinessPeriodDTO.of(purchaseOrder.getRentalPeriod().getStartDate(),purchaseOrder.getRentalPeriod().getEndDate()));
        dto.setTotal(purchaseOrder.getTotal());
        dto.setStatus(purchaseOrder.getStatus());

        try {
            switch (dto.getStatus()) {
                case PENDING:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .acceptPurchaseOrder(dto.get_id())).toString(),
                            "accept", POST));
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .rejectPurchaseOrder(dto.get_id())).toString(),
                            "reject", DELETE));
                    break;
                case OPEN:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .closePurchaseOrder(dto.get_id())).toString(),
                            "close", POST));
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return dto;
    }
}
