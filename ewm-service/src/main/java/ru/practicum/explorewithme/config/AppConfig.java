package ru.practicum.explorewithme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the application.
 */
@Configuration
@EnableTransactionManagement
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
}
