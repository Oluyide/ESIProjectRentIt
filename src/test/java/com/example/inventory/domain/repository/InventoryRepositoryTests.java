package com.example.inventory.domain.repository;

import com.example.DemoApplication;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Sql(scripts="plants-dataset.sql")
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTests {
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;
    @Autowired
    PlantReservationRepository plantReservationRepository;


    @Test
    public void findAvailableTest() {
        PlantInventoryEntry entry = plantInventoryEntryRepository.findOne("1");
        PlantInventoryItem item = plantInventoryItemRepository.findOneByPlantInfo(entry);

        assertThat(inventoryRepository.findAvailable(entry.getName().toLowerCase(),
                LocalDate.of(2017, 2, 20), LocalDate.of(2017, 2, 25)))
                .contains(entry);

        plantReservationRepository.save(PlantReservation.of(null, item, BusinessPeriod.of(LocalDate.of(2017, 2, 20), LocalDate.of(2017, 2, 25))));

        assertThat(inventoryRepository.findAvailable(entry.getName().toLowerCase(), LocalDate.of(2017, 2, 20), LocalDate.of(2017, 2, 25)))
                .doesNotContain(entry);
    }
}
