package com.ead.authuser.controllers;

import com.ead.authuser.controllers.dtos.UserRecordDto;
import com.ead.authuser.service.UserService;
import com.ead.authuser.validations.UserValidator;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);

    final UserService userService;
    final UserValidator userValidator;

    public AuthenticationController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody
            @Validated(UserRecordDto.UserView.RegistrationPost.class)
            @JsonView(UserRecordDto.UserView.RegistrationPost.class) UserRecordDto userRecordDto,
            Errors errors
    ) {
        logger.debug("POST registerUser userRecordDTO receveid: {}", userRecordDto);
        userValidator.validate(userRecordDto, errors);
        if (errors.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());

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
