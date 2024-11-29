package com.ead.authuser.service.impl;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.controllers.dtos.UserRecordDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ead.authuser.enums.UserType.INSTRUCTOR;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final UserCourseRepository userCourseRepository;
    final CourseClient courseClient;

    public UserServiceImpl(UserRepository userRepository, UserCourseRepository userCourseRepository, CourseClient courseClient) {
        this.userRepository = userRepository;
        this.userCourseRepository = userCourseRepository;
        this.courseClient = courseClient;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        Optional<UserModel> userModelOptional = userRepository.findById(userId);

        if (userModelOptional.isEmpty()) {
            throw new NotFoundException("User not found!");
        }

        return userModelOptional;
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {
        List<UserCourseModel> userCourseModelList = userCourseRepository.findAllUserCourseIntoUser(userModel.getUserId());
        boolean deleteUserCourseInCourse = false;

        if (!userCourseModelList.isEmpty()) {
            userCourseRepository.deleteAll(userCourseModelList);
            deleteUserCourseInCourse = true;
        }

        userRepository.delete(userModel);
        if (deleteUserCourseInCourse) courseClient.deleteUserCourseInCourse(userModel.getUserId());
    }

    @Override
    public UserModel registerUser(UserRecordDto userRecordDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);

        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserModel updateUser(UserRecordDto userRecordDto, UserModel userModel) {
        userModel.setFullName(userRecordDto.fullName());
        userModel.setPhoneNumber(userRecordDto.phoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public UserModel updatePassword(UserRecordDto userRecordDto, UserModel userModel) {
        userModel.setPassword(userRecordDto.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateImage(UserRecordDto userRecordDto, UserModel userModel) {
        userModel.setImageUrl(userRecordDto.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public UserModel registerInstructor(UserModel userModel) {
        userModel.setUserType(INSTRUCTOR);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }
}
