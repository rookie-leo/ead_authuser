package com.ead.authuser.client;

import com.ead.authuser.controllers.dtos.CourseRecordDto;
import com.ead.authuser.controllers.dtos.ResponsePageDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class CourseClient {

    Logger logger = LogManager.getLogger(CourseClient.class);

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

//    @Retry(name = "retryInstance", fallbackMethod = "retryfallback")
    @CircuitBreaker(name = "circuitbreakerInstance"/*, fallbackMethod = "circuitbreakerfallback"*/)
    public Page<CourseRecordDto> getAllCoursesByUser(UUID userId, Pageable pageable) {
        String url = baseUrlCourse + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        logger.debug("GET Request to URL: {}", url);

        try {
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<CourseRecordDto>>() {});
        } catch (RestClientException ex) {
            logger.error("Error Request RestClient with cause: {}", ex.getMessage());
            throw new RuntimeException("Error Request RestClient", ex);
        }
    }

    public Page<CourseRecordDto> retryfallback(UUID userId, Pageable pageable, Throwable t) {
        logger.error("Inside retry retryfallback, cause - {}", t.toString());
        List<CourseRecordDto> searchResult = new ArrayList<>();

        return new PageImpl<>(searchResult);
    }

    public Page<CourseRecordDto> circuitbreakerfallback(UUID userId, Pageable pageable, Throwable t) {
        logger.error("Inside circuit breaker fallback, cause - {}", t.toString());
        List<CourseRecordDto> searchResult = new ArrayList<>();

        return new PageImpl<>(searchResult);
    }
}
