package com.ead.authuser.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_USERS_COURSES")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID courseId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserModel user;

    public UserCourseModel(){}

    public UserCourseModel(UUID id, UUID courseId, UserModel userModel) {
        this.id = id;
        this.courseId = courseId;
        this.user = userModel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
