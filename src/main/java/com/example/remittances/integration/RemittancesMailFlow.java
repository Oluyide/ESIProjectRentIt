package com.example.remittances.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 04/05/2017.
 */
@Service
public class RemittancesMailFlow extends RemittancesFlow{

    //@Value("${gmail.username}")
    String gmailUsername="esi2017.g17";
    //@Value("${gmail.password}")
    String gmailPassword="nocheese";

    @Bean
    IntegrationFlow inboundMail() {
        System.out.println("creds: "+gmailUsername+gmailPassword);
        return IntegrationFlows.from(Mail.imapInboundAdapter(
                String.format("imaps://%s:%s@imap.gmail.com:993/INBOX", gmailUsername, gmailPassword)
                ).selectorExpression("subject matches '.*remittance.*'"),
                e -> e.autoStartup(true)
                        .poller(Pollers.fixedDelay(40000))
        ).transform("@remittancesProcessor.extractRemittance(payload)")
                .channel("router-channel")
                .get();
    }
}
