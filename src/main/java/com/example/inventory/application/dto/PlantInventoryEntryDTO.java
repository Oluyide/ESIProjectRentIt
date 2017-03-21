package com.example.inventory.application.dto;

import com.example.common.rest.ResourceSupport;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    String _id;
    String name;
    String description;
    BigDecimal price;
}
