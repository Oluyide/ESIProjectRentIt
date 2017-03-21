package com.example.sales.rest.controller;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.rest.ExtendedLink;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.service.InventoryService;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.*;
/**
 * Created by lgarcia on 3/10/2017.
 */
@RestController
@RequestMapping("/api/sales/orders")
public class SalesRestController {
    @Autowired
    SalesService salesService;

    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO poDTO) throws Exception {
        poDTO = salesService.createPurchaseOrder(poDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(poDTO.getId().getHref()));
        return new ResponseEntity<PurchaseOrderDTO>(poDTO, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<PurchaseOrderDTO> getAllPurchaseOrders() throws Exception {
        return salesService.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrderDTO showPurchaseOrder(@PathVariable String id) throws Exception {
        PurchaseOrderDTO poDTO = salesService.findPurchaseOrder(id);
        return poDTO;
    }

    @PostMapping("/{id}/accept")
    public PurchaseOrderDTO acceptPurchaseOrder(@PathVariable String id) throws Exception {
        return salesService.acceptPurchaseOrder(id);
    }

    @DeleteMapping("/{id}/accept")
    public PurchaseOrderDTO rejectPurchaseOrder(@PathVariable String id) throws Exception {
        return salesService.rejectPurchaseOrder(id);
    }

//    @DeleteMapping("/{id}")
//    public PurchaseOrderDTO closePurchaseOrder(@PathVariable String id) throws Exception {
//        return salesService.closePurchaseOrder(id);
//    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public String bindExceptionHandler(Exception ex) {
        return ex.getMessage();
    }
}
