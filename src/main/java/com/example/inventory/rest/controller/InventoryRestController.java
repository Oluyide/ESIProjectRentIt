package com.example.inventory.rest.controller;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.service.InventoryService;
import com.example.inventory.domain.model.PlantInventoryItemStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/plants")
public class InventoryRestController {
    @Autowired
    InventoryService inventoryService;

    @GetMapping
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {

        if (plantName.isPresent() && startDate.isPresent() && endDate.isPresent()) {
            if (endDate.get().isBefore(startDate.get()))
                throw new IllegalArgumentException("Something wrong with the requested period ('endDate' happens before 'startDate')");
            return inventoryService.findAvailablePlants(plantName.get(), startDate.get(), endDate.get());
        } else
            throw new IllegalArgumentException(
                    String.format("Wrong number of parameters: Name='%s', Start date='%s', End date='%s'",
                            plantName.get(), startDate.get(), endDate.get()));
    }

    @GetMapping("/{id}")
    public PlantInventoryEntryDTO show(@PathVariable String id) throws PlantNotFoundException {
        return inventoryService.findPlant(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception exc) {
        return exc.getMessage();
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception exc) {
        return exc.getMessage();
    }

    @PostMapping("/{id}/dispatched")
    public void dispatchPlant(@PathVariable String id) throws PlantNotFoundException{
        inventoryService.dispatchPlant(id);
    }

    @PostMapping("/{id}/delivered")
    public void deliverPlant(@PathVariable String id) throws PlantNotFoundException{
        inventoryService.deliverPlant(id);
    }

    @PostMapping("/{id}/rejected")
    public void rejectPlant(@PathVariable String id) throws PlantNotFoundException{
        inventoryService.rejectPlant(id);
    }

    @PostMapping("/{id}/returned")
    public void returnPlant(@PathVariable String id) throws PlantNotFoundException{
        inventoryService.returnPlant(id);
    }

}

