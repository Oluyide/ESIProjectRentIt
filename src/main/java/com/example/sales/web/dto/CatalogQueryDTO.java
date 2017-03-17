package com.example.sales.web.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;
}
