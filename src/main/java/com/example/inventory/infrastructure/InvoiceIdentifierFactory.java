package com.example.inventory.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by gerson on 16/05/17.
 */
@Service
public class InvoiceIdentifierFactory {
    public String nextInvoiceID() {
        return UUID.randomUUID().toString();
    }
}
