package com.ead.authuser.controllers.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourseRecordDto(
        UUID userId,
        @NotNull(message = "CourseId is mandatory") UUID courseId
) {
}
