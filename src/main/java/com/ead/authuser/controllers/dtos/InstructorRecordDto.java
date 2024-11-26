package com.ead.authuser.controllers.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InstructorRecordDto(@NotNull(message = "UUID is mandatory!") UUID userId) {
}
