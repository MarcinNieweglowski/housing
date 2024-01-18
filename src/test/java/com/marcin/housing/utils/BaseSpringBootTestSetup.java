package com.marcin.housing.utils;

import com.marcin.housing.HousingApplication;
import com.marcin.housing.scheduler.HousingDataScheduler;
import com.marcin.housing.service.EmailService;
import com.marcin.housing.service.HousingRepository;
import com.marcin.housing.service.HousingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HousingApplication.class, webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseSpringBootTestSetup {

    @Autowired
    protected HousingRepository housingRepository;

    @Autowired
    protected HousingService housingService;

    @MockBean
    protected EmailService emailService;

    @Autowired
    protected HousingDataScheduler scheduler;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        housingRepository.deleteAll();
    }
}
