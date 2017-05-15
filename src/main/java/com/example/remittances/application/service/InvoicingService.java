package com.example.remittances.application.service;

import com.example.common.integration.MailIntegration;
import com.example.remittances.application.dto.RemittanceDTO;
import com.example.remittances.domain.model.Invoice;
import com.example.remittances.domain.model.InvoiceStatus;
import com.example.remittances.domain.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by gerson on 15/05/17.
 */

@Service
public class InvoicingService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MailIntegration mailIntegration;

    public void processRemittance(RemittanceDTO remittance){
        System.out.println("Will process remittance " + remittance);

        Invoice invoice = invoiceRepository.getOne(remittance.getInvoiceId());

        if (invoice != null){


            if (remittance.getTotal().compareTo(invoice.getTotalPrice()) == 0){
                invoice.handleStatusChange(InvoiceStatus.PAID);

                try {
                    String body = "Dear customer,\n\nThe remittance for invoice number " + invoice.get_id() + " has been approved" +
                            "and the invoice has bee marked as paid. Thank you.";
                    mailIntegration.sendMail("esi2017.e17@gmail.com", "Remittance approved", body);
                }
                catch(Exception e){}

            }
            else{
                invoice.handleStatusChange(InvoiceStatus.REJECTED);

                try {
                    String body = "Dear customer,\n\nThe remittance for invoice number " + invoice.get_id() + " has been rejected " +
                            "because the total price does not coincide with our records. Please check it and try again.";
                    mailIntegration.sendMail("esi2017.e17@gmail.com", "Remittance rejected", body);
                }
                catch(Exception e){}
            }

            invoiceRepository.save(invoice);
        }
        else{
            System.err.println("No invoice associated with this ID");

            try {
                String body = "Dear customer,\n\nThe remittance for invoice number " + invoice.get_id() + " has been rejected " +
                        "because there's no invoice associated with that ID. Please check it and try again.";
                mailIntegration.sendMail("esi2017.e17@gmail.com", "Remittance rejected", body);
            }
            catch(Exception e){}
        }

    }
}
