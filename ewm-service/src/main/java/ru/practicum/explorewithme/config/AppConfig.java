package ru.practicum.explorewithme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Configuration class for the application.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and configures a RestTemplate bean.
     *
     * @return the configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    /**
     * Creates and configures an ExecutorService bean.
     *
     * @return the configured ExecutorService
     */
    @Bean
    public ExecutorService taskExecutor() {
        int numCores = Runtime.getRuntime().availableProcessors();
        int numThreads = numCores * 2;
        return Executors.newFixedThreadPool(numThreads);
    }
}
