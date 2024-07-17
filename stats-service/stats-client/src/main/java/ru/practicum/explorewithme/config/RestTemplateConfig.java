package ru.practicum.explorewithme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for RestTemplate.
 */
@Configuration
public class RestTemplateConfig {

    @Value("${server.url}")
    private String serverUrl;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        RestTemplate restTemplate = builder
                .rootUri(serverUrl)
                .build();
        restTemplate.setRequestFactory(factory);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(logRequest());
        interceptors.add(logResponse());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    private ClientHttpRequestInterceptor logRequest() {
        return (request, body, execution) -> {
            System.out.println("Request: " + request.getMethod() + " " + request.getURI());
            request.getHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return execution.execute(request, body);
        };
    }

    private ClientHttpRequestInterceptor logResponse() {
        return (request, body, execution) -> {
            var response = execution.execute(request, body);
            System.out.println("Response Status: " + response.getStatusCode());
            response.getHeaders().forEach((name, values) -> values.forEach(value -> System.out.println(name + ": " + value)));
            return response;
        };
    }
}
