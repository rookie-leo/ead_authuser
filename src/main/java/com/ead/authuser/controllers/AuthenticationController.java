package com.ead.authuser.controllers;

import com.ead.authuser.controllers.dtos.UserRecordDto;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);

    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody @Validated(UserRecordDto.UserView.RegistrationPost.class) @JsonView(UserRecordDto.UserView.RegistrationPost.class) UserRecordDto userRecordDto) {
        logger.debug("POST registerUser userRecordDTO receveid: {}", userRecordDto);
        if (userService.existsByUserName(userRecordDto.userName())) {
            logger.warn("Username {} is Already Taken!", userRecordDto.userName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is Already Taken!");
        }

        if (userService.existsByEmail(userRecordDto.email())) {
            logger.warn("Email {} is Already Taken!", userRecordDto.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is Already Taken!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRecordDto));
    }

    @GetMapping("/logs")
    public String index() {
        logger.trace("TRACE");
        logger.debug("DEBUG");
        logger.info("INFO");
        logger.warn("WARN");
        logger.error("ERROR");
        return "Logging Spring Boot...";
    }
}
