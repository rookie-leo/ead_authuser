package com.ead.authuser.controllers.dtos;

import com.ead.authuser.enums.CourseLevel;
import com.ead.authuser.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CourseRecordDto(
        UUID courseId,

        String name,

        String description,

        CourseStatus courseStatus,

        CourseLevel courseLevel,

        UUID userInstructor,

        String imageUrl
) {
}
