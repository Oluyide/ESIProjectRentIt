package com.example.remittances.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 04/05/2017.
 */
@Service
public class RemittancesHttpFlow extends RemittancesFlow{
    @Bean
    IntegrationFlow inboundHttpGateway() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/remittances")
                        .requestPayloadType(String.class)
        )
                .channel("router-channel")
                .get();
    }
}
