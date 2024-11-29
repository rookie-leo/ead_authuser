package com.ead.authuser.controllers;

import com.ead.authuser.controllers.dtos.InstructorRecordDto;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.service.UserService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    Logger log = LogManager.getLogger(InstructorController.class);

    final UserService userService;

    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> saveSubscriptionInstructor(@RequestBody @Valid InstructorRecordDto instructorRecordDto) {
        if (userService.findById(instructorRecordDto.userId()).isEmpty()) {
            log.error("UserId: {} not found!", instructorRecordDto.userId());
            throw new NotFoundException("UserId not found!");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerInstructor(userService.findById(instructorRecordDto.userId()).get()));
    }
}
