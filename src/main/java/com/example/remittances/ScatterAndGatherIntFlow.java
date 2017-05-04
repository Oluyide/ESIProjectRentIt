package com.example.remittances;

import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.remittances.application.dto.RemittanceDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.scripting.Scripts;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.GenericMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@MessagingGateway
interface RentalService {
    @Gateway(requestChannel = "requestChannel", replyChannel = "replyChannel")
    Object findPlants(@Payload String name, @Header("startDate") LocalDate startDate, @Header("endDate") LocalDate endDate);
}
@Configuration
public class ScatterAndGatherIntFlow {
    @Bean
    IntegrationFlow scatter() {
        return IntegrationFlows.from("requestChannel")
                .publishSubscribeChannel(conf ->
                        conf.applySequence(true)
                                .subscribe( flow -> flow.channel("requestChannel-buildit"))
                ).get();
    }

    @Bean
    IntegrationFlow gather() {
        return IntegrationFlows.from("gatherChannel")
                .aggregate( spec -> spec.outputProcessor( proc -> {
                    List<RemittanceDTO> list = new ArrayList<>();
                    for (Message msg: proc.getMessages())
                        list.addAll(Arrays.asList((RemittanceDTO[]) msg.getPayload()));
                    System.out.println(list);
                    return new GenericMessage<>(list);
                }))
                .channel("replyChannel")
                .get();
    }

    @Bean
    IntegrationFlow buildititOutboundGW() {
        return IntegrationFlows.from("requestChannel-buildit")
                .handle(Http
                        .outboundGateway("http://localhost:3000/api/inventory/plants")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(RemittanceDTO[].class))
                .channel("gatherChannel")
                .get();
    }

    @Bean
    IntegrationFlow siren2halTransformation() {
        return IntegrationFlows.from("siren2halChannel")
                .transform(Scripts.script(new ByteArrayResource(
                        (       "var obj = JSON.parse(payload); " +
                                "var newObj = obj.entities.map(function (e) { " +
                                "                     return {name: e.properties.name," +
                                "							  description: e.properties.description," +
                                "							  price: e.properties.price }; " +
                                "             });" +
                                "JSON.stringify(newObj);").getBytes()
                ))
                        .lang("javascript"))
                .channel("json2objectChannel")
                .get();
    }

    @Bean
    IntegrationFlow json2Object() {
        return IntegrationFlows.from("json2objectChannel")
                .transform(Transformers.fromJson(RemittanceDTO[].class))
                .channel("gatherChannel")
                .get();
    }
}
