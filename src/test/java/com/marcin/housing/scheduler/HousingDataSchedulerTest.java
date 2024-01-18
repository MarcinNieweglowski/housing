package com.marcin.housing.scheduler;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.marcin.housing.utils.BaseSpringBootTestSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.lang.reflect.Method;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static com.marcin.housing.utils.WireMockJsonResponses.HOUSING_CLIENT_FETCH_HOUSING_DATA_EMPTY_RESPONSE;
import static com.marcin.housing.utils.WireMockJsonResponses.HOUSING_CLIENT_FETCH_HOUSING_DATA_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

class HousingDataSchedulerTest extends BaseSpringBootTestSetup {

    private static final String URL_REGEX = "/.*";

    @RegisterExtension
    private static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("housing.client.base-url", wireMockServer::baseUrl);
    }

    @Test
    void fetchHousingData_shouldBeMarkedAsScheduled() throws NoSuchMethodException {
        Method fetchHousingDataMethod = HousingDataScheduler.class.getMethod("fetchHousingData");
        Scheduled scheduledAnnotation = fetchHousingDataMethod.getAnnotation(Scheduled.class);
        assertThat(scheduledAnnotation).isNotNull();
    }

    @Test
    void fetchHousingData_shouldBeMarkedAsRetryable() throws NoSuchMethodException {
        Method fetchHousingDataMethod = HousingDataScheduler.class.getMethod("fetchHousingData");
        Retryable retryableAnnotation = fetchHousingDataMethod.getAnnotation(Retryable.class);
        assertThat(retryableAnnotation).isNotNull();
        assertThat(retryableAnnotation.maxAttemptsExpression()).isEqualTo("${housing.cron.retry.maxAttempts}");
        assertThat(retryableAnnotation.retryFor().length).isEqualTo(1);
        assertThat(retryableAnnotation.retryFor()).contains(Exception.class);
        assertThat(retryableAnnotation.backoff().delayExpression()).isEqualTo("${housing.cron.retry.delay}");
    }

    @Test
    void sendErrorEmail_shouldBeMarkedAsRecover() throws NoSuchMethodException {
        Method sendErrorEmail = HousingDataScheduler.class.getMethod("sendErrorEmail");
        Recover recoverAnnotation = sendErrorEmail.getAnnotation(Recover.class);
        assertThat(recoverAnnotation).isNotNull();
    }

    @Test
    void fetchHousingData_shouldStoreTheReturnedData(WireMockRuntimeInfo wmi) {
        wireMockServer.stubFor(get(urlMatching(URL_REGEX))
                .willReturn(ok(HOUSING_CLIENT_FETCH_HOUSING_DATA_RESPONSE).withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        scheduler.fetchHousingData();

        assertThat(housingRepository.findAll().size()).isEqualTo(5);
    }

    @Test
    void fetchHousingData_shouldNotFail_ifNoHousingDataWasReturned(WireMockRuntimeInfo wmi) {
        wireMockServer.stubFor(get(urlMatching(URL_REGEX))
                .willReturn(ok(HOUSING_CLIENT_FETCH_HOUSING_DATA_EMPTY_RESPONSE).withHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        scheduler.fetchHousingData();

        assertThat(housingRepository.findAll().isEmpty()).isTrue();
    }
}