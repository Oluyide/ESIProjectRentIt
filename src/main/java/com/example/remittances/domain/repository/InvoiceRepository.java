package com.example.remittances.domain.repository;

import com.example.remittances.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gerson on 15/05/17.
 */

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>{
}
