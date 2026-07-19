package com.mzrt.erp_lite.adapter.out.persistence.jsonplaceholder.config;

import com.mzrt.erp_lite.adapter.out.persistence.jsonplaceholder.model.JsonPlaceholderConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RestClientConfig {

    private final JsonPlaceholderConfigModel jsonConfig;



    @Bean(name = "jsonPlaceHolder")
    @ConditionalOnProperty(
            prefix = "jsonplaceholder.api",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(jsonConfig.baseUrl())
                .requestInterceptors(interceptors -> {
                    interceptors.add(loggingInterceptor());
                    interceptors.add(errorLoggingInterceptor());
                })
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
                })
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor(){
        return (req,body, execution) -> {
            log.info("Calling JsonPlaceHolder API");
            final long  startTime = System.currentTimeMillis();
            try (var response = execution.execute(req, body)) {
                final long endTime = System.currentTimeMillis() - startTime;
                log.info("Method: {} URI: {} Headers: {} Execution time: {} ms Status: {}", req.getMethod(), req.getURI(), req.getHeaders(), endTime, response.getStatusCode());
                return response;
            }
        };
    }

    private ClientHttpRequestInterceptor errorLoggingInterceptor(){
        return (req,body, execution) -> {
         try{
             return execution.execute(req, body);
         } catch (Exception ex){
             log.error("Error message: {}", ex.getMessage());
             throw ex;
         }
        };
    }

}
