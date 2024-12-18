package com.ead.authuser.client;

import com.ead.authuser.controllers.dtos.CourseRecordDto;
import com.ead.authuser.controllers.dtos.ResponsePageDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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

    public void deleteUserCourseInCourse(UUID userId) {
        String url = baseUrlCourse + "/courses/users/" + userId;
        logger.debug("DELETE Request to URL: {}", url);

        try {
            restClient.delete()
                    .uri(url)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            logger.error("Error Request DELETE RestClient with cause: {}", ex.getMessage());
            throw new RuntimeException("Error Request DELETE RestClient", ex);
        }
    }
}
