package com.example.sales.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalesIdentifierFactory {
    public String nextPurchaseOrderID() {
        return UUID.randomUUID().toString();
    }
}
