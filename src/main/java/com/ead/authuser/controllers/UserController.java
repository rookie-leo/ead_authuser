package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.AuthenticationCurrentUserService;
import com.ead.authuser.configs.security.UserDetailsImpl;
import com.ead.authuser.controllers.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    Logger logger = LogManager.getLogger(UserController.class);

    final UserService userService;
    final PasswordEncoder passwordEncoder;
    final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(
            SpecificationTemplate.UserSpec spec,
            Pageable pageable,
            Authentication authentication
    ) {
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        logger.info("Authentication {} ", userDetails.getUsername());

        Page<UserModel> userModelPage = userService.findAll(spec, pageable);

        if (!userModelPage.isEmpty()) {
            userModelPage.toList().forEach(user ->
                    user.add(linkTo(methodOn(UserController.class).getUser(user.getUserId())).withSelfRel())
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable(value = "userId") UUID userId) {
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if (currentUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(userId).get());
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId) {
        logger.debug("DELETE deleteUser userId: {}", userId);
        userService.delete(userService.findById(userId).get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDto.UserView.UserPut.class) @JsonView(UserRecordDto.UserView.UserPut.class) UserRecordDto userRecordDto
    ) {
        logger.debug("PUT updateUser userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userRecordDto, userService.findById(userId).get()));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDto.UserView.PasswordPut.class) @JsonView(UserRecordDto.UserView.PasswordPut.class) UserRecordDto userRecordDto
    ) {
        logger.debug("PUT updatePassword userId: {}", userId);
        var userModel = userService.findById(userId).get();

        if (!passwordEncoder.matches(userRecordDto.oldPassword(), userModel.getPassword())) {
            logger.warn("Mismatched old password: {}", userRecordDto.oldPassword());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mismatched old password!");
        }

        userService.updatePassword(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @Validated(UserRecordDto.UserView.ImagePut.class) @JsonView(UserRecordDto.UserView.ImagePut.class) UserRecordDto userRecordDto
    ) {
        logger.debug("PUT updateImage userId: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateImage(userRecordDto, userService.findById(userId).get()));
    }
}
