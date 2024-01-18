package com.marcin.housing.scheduler;

import com.marcin.housing.model.Housing;
import com.marcin.housing.model.Region;
import com.marcin.housing.service.EmailService;
import com.marcin.housing.service.HousingClientImpl;
import com.marcin.housing.service.HousingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling
@EnableRetry
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HousingDataScheduler {

    private final HousingService housingService;
    private final HousingClientImpl housingClientImpl;
    private final EmailService emailService;

    @Scheduled(cron = "${housing.cron.expression}")
    @Retryable(retryFor = Exception.class, maxAttemptsExpression = "${housing.cron.retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${housing.cron.retry.delay}"))
    public void fetchHousingData() {
        log.info("Scheduler cron job initiated - about to fetch housing data for all regions");
        List<Housing> housings = Arrays.asList(Region.values()).stream()
                .map(housingClientImpl::fetchHousingData)
                .flatMap(List::stream)
                .toList();
        log.info("Fetched {} housings data", housings.size());

        housingService.saveHousingData(housings);
    }

    @Recover
    public void sendErrorEmail() {
        log.error("Fetching data from external provider failed, sending email notification");
        emailService.sendErrorEmail();
    }
}
