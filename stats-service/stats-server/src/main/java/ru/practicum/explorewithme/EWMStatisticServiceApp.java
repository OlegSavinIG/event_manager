package ru.practicum.explorewithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for EWM Statistic Service.
 */
@SpringBootApplication
public class EWMStatisticServiceApp {
    /**
     * The main method which serves as
     * the entry point for the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EWMStatisticServiceApp.class, args);
    }
}
