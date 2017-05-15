package com.example.remittances.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by gerson on 15/05/17.
 */
@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName="of")
public class Invoice {
    @Id
    String _id;

    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    InvoiceStatus invoiceStatus;

    public void handleStatusChange(InvoiceStatus status){
        invoiceStatus = status;
    }
}