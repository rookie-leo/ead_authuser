package com.ead.authuser.specifications;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import jakarta.persistence.criteria.Join;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "userType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "userName", spec = Like.class),
            @Spec(path = "fullName", spec = LikeIgnoreCase.class)
    })
    public interface UserSpec extends Specification<UserModel> {}

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return ((root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<UserModel, UserCourseModel> userJoin = root.join("usersCourses");
            return criteriaBuilder.equal(userJoin.get("courseId"), courseId);
        });
    }
}
