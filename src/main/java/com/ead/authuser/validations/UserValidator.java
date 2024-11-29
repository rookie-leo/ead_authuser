package com.ead.authuser.validations;

import com.ead.authuser.controllers.dtos.UserRecordDto;
import com.ead.authuser.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    Logger log = LogManager.getLogger(UserValidator.class);

    private final Validator validator;
    final UserService userService;

    public UserValidator(@Qualifier("defaultValidator") Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRecordDto userRecordDto = (UserRecordDto) target;
        validator.validate(userRecordDto, errors);

        if (!errors.hasErrors()) {
            validateUserName(userRecordDto, errors);
            validateEmail(userRecordDto, errors);
        }
    }

    private void validateUserName(UserRecordDto userRecordDto, Errors errors) {
        if (userService.existsByUserName(userRecordDto.userName())) {
            errors.rejectValue("userName", "userNameConflict", "Username is Already take!");
            log.error("Error validation userName: {}", userRecordDto.userName());
        }
    }

    private void validateEmail(UserRecordDto userRecordDto, Errors errors) {
        if (userService.existsByEmail(userRecordDto.email())) {
            errors.rejectValue("email", "emailConflict", "Email is Already take!");
            log.error("Error validation email: {}", userRecordDto.email());
        }
    }
}
