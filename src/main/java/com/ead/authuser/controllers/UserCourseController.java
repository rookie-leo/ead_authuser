package com.ead.authuser.controllers;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.configs.security.AuthenticationCurrentUserService;
import com.ead.authuser.configs.security.UserDetailsImpl;
import com.ead.authuser.controllers.dtos.CourseRecordDto;
import com.ead.authuser.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserCourseController {

    final AuthenticationCurrentUserService authenticationCurrentUserService;
    final CourseClient courseClient;
    final UserService userService;

    public UserCourseController(AuthenticationCurrentUserService authenticationCurrentUserService, CourseClient courseClient, UserService userService) {
        this.authenticationCurrentUserService = authenticationCurrentUserService;
        this.courseClient = courseClient;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseRecordDto>> getAllCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId,
            @RequestHeader("Authorization") String token
    ) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();

        if (userDetails.getUserId().equals(userId) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            userService.findById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable, token));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
