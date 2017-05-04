package com.example.remittances;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 04/05/2017.
 */
@Service
public class RemittanceService {

    @Bean
    IntegrationFlow inboundHttpGateway() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/remittances")
                        .requestPayloadType(String.class)
        )
                .channel("router-channel")
                .get();
    }

    @Bean
    IntegrationFlow router() {
        return IntegrationFlows.from("router-channel")
                .route("true", routes -> routes
                        .subFlowMapping("true", subflow -> subflow.channel("normaltrack-channel"))
                )
                .get();
    }

    @Bean
    IntegrationFlow normalTrack() {
        return IntegrationFlows.from("normaltrack-channel")
                .handle(System.out::println)
                .get();
    }


    @Value("${gmail.username}")
    String gmailUsername;
    @Value("${gmail.password}")
    String gmailPassword;

    @Bean
    IntegrationFlow inboundMail() {
        System.out.println("creds: "+gmailUsername+gmailPassword);
        return IntegrationFlows.from(Mail.imapInboundAdapter(
                String.format("imaps://%s:%s@imap.gmail.com:993/INBOX", gmailUsername, gmailPassword)
                ).selectorExpression("subject matches '.*remittance.*'"),
                e -> e.autoStartup(true)
                        .poller(Pollers.fixedDelay(40000))
        ).transform("@remittanceProcessor.extractRemittance(payload)")
                .channel("router-channel")
                .get();
    }

}
