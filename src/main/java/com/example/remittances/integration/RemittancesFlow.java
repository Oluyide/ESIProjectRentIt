package com.example.remittances.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 04/05/2017.
 */
@Service
public abstract class RemittancesFlow {

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
                .handle("invoicingService", "processRemittance")
                .get();
    }
}
