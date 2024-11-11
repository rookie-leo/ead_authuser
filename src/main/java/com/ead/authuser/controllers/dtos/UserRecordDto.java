package com.ead.authuser.controllers.dtos;

public record UserRecordDto(
        String userName,
        String email,
        String password,
        String oldPassword,
        String fullName,
        String phoneNumber,
        String imageUrl
) {
}
