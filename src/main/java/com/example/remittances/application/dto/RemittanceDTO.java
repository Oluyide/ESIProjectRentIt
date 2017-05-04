package com.example.remittances.application.dto;

import com.example.common.rest.ResourceSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by gkgranada on 04/05/2017.
 */
@Data
@AllArgsConstructor(staticName = "of")
public class RemittanceDTO extends ResourceSupport {
    String _id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate date;
    String invoiceId;
    BigDecimal total;
}
