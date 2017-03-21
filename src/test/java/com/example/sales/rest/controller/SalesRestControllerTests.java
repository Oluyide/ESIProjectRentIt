package com.example.sales.rest.controller;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by Gerson Noboa on 21/3/2017.
 */
public class SalesRestControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }

    @Test
    @Sql("plants-dataset.sql")
    public void testPurchaseOrderAcceptance() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/inventory/plants?name=Exc&startDate=2016-03-14&endDate=2016-03-25"))
                .andReturn();
        List<PlantInventoryEntryDTO> plants =
                mapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<PlantInventoryEntryDTO>>() { });

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plants.get(2));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));

        result = mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", not(isEmptyOrNullString())))
                .andReturn();

        order = mapper.readValue(result.getResponse().getContentAsString(), PurchaseOrderDTO.class);

        assertThat(order.get_xlink("accept"), is(notNullValue()));

        mockMvc.perform(post(order.get_xlink("accept").getHref()))
                .andReturn();
    }


}
