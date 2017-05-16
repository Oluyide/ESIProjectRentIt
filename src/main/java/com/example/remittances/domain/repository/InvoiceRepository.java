package com.example.remittances.domain.repository;

import com.example.remittances.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gerson on 15/05/17.
 */

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>{
    @Query("select i from Invoice i where i.invoiceStatus = com.example.remittances.domain.model.InvoiceStatus.APPROVED")
    List<Invoice> findApproved();
}
