package com.example.remittances.integration;

import com.example.remittances.application.dto.RemittanceDTO;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface RemittancesGateway {
    @Gateway(requestChannel = "sendRemittance-http-channel")
    public void sendRemittance(RemittanceDTO remittanceDTO);
}