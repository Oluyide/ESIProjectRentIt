package com.example.remittances.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.rest.ResourceSupport;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.sales.domain.model.POStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by gkgranada on 04/05/2017.
 */
@Data
public class RemittanceDTO extends ResourceSupport {
    String _id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate date;
    BigDecimal total;
}
