package com.example.utils;

import com.example.backend.persistence.domain.backend.User;
import com.example.web.controllers.ForgotPasswordController;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;

@UtilityClass
public class UserUtils {

    public static User createBasicUser(String username, String email) {
        return User.builder()
                .username(username)
                .password("password")
                .email(email)
                .firstName("firstName")
                .lastName("lastName")
                .phoneNumber("123456789")
                .country("GB")
                .enabled(true)
                .description("a basic user")
                .profileImageUrl("https://balbla.imgaes.com/basicuser")
                .build();
    }

    public static String createPasswordResetUrl(HttpServletRequest request, long userId, String token) {
        String passwordResetUrl =
                request.getScheme() +
                        "://" +
                        request.getServerName() +
                        ":" +
                        request.getServerPort() +
                        request.getContextPath() +
                        ForgotPasswordController.CHANGE_PASSWORD_PATH +
                        "?id=" +
                        userId +
                        "&token=" +
                        token;

        return passwordResetUrl;
    }
}
