package org.softuni.bookshopbg.service.impl;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softuni.bookshopbg.service.MonitoringService;
import org.springframework.stereotype.Service;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private Logger LOGGER = LoggerFactory.getLogger(MonitoringServiceImpl.class);

    private final Counter bookSearches;

    public MonitoringServiceImpl(MeterRegistry meterRegistry) {
        bookSearches = Counter
                .builder("book_search_cnt")
                .description("How many book searched we have performed")
                .register(meterRegistry);
    }

    @Override
    public void logBookSearch() {
        LOGGER.info("Offer search was performed.");
        bookSearches.increment();
    }
}