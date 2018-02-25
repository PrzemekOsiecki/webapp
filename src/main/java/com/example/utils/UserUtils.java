package com.example.utils;

import com.example.backend.persistence.domain.backend.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtils {

    public User createBasicUser() {
        return User.builder()
                .username("user")
                .password("password")
                .email("email@com.pl")
                .firstName("firstName")
                .lastName("lastName")
                .phoneNumber("123456789")
                .country("GB")
                .enabled(true)
                .description("a basic user")
                .profileImageUrl("https://balbla.imgaes.com/basicuser")
                .build();
    }
}
