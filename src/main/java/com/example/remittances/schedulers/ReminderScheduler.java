package com.example.remittances.schedulers;

import com.example.common.integration.MailIntegration;
import com.example.remittances.domain.model.Invoice;
import com.example.remittances.domain.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sharedvi on 5/16/17.
 */
@Component
public class ReminderScheduler {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MailIntegration mailIntegration;

    @Scheduled(fixedRate = 86400000)
    public void sendReminders() {
        System.out.println("Sending reminders");
        List<Invoice> invoices = invoiceRepository.findApproved();
        for (Invoice invoice:invoices) {
            try {
                String body = "Dear customer,\n\n You approved invoice #" + invoice.get_id() + ", but have not submitted payment confirmation. " +
                        "We're waiting for your payment. Thank you.";
                mailIntegration.sendMail("esi2017.e17@gmail.com", "Reminder: invoice "+ invoice.get_id() , body, null, null);
            } catch (Exception e) {
        }

        }
    }

}
