package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.controllers.dtos.JwtRecordDto;
import com.ead.authuser.controllers.dtos.LoginRecordDto;
import com.ead.authuser.controllers.dtos.UserRecordDto;
import com.ead.authuser.service.UserService;
import com.ead.authuser.validations.UserValidator;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    Logger logger = LogManager.getLogger(AuthenticationController.class);

    final UserService userService;
    final UserValidator userValidator;
    final JwtProvider jwtProvider;
    final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService, UserValidator userValidator, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
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

    @PostMapping("/login")
    public ResponseEntity<JwtRecordDto> authenticateUser(@RequestBody @Valid LoginRecordDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtRecordDto(jwtProvider.generateJwt(authentication)));
    }

    /*
     * Metodo apenas para caso de estudo
     */
    @PostMapping("/signup/admin/usr")
    public ResponseEntity<Object> registerUserAdmin(
            @RequestBody
            @Validated(UserRecordDto.UserView.RegistrationPost.class)
            @JsonView(UserRecordDto.UserView.RegistrationPost.class)
            UserRecordDto userRecordDto,
            Errors errors
    ) {
        userValidator.validate(userRecordDto, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUserAdmin(userRecordDto));
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
