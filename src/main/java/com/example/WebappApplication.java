package com.example;

import com.example.backend.persistence.domain.backend.Role;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.backend.service.UserService;
import com.example.enums.PlansEnum;
import com.example.enums.RolesEnum;
import com.example.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class WebappApplication implements CommandLineRunner {
	
    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = UserUtils.createBasicUser();
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
        log.debug("Creating user with name = {}", user.getUsername());
        userService.createUser(user, PlansEnum.BASIC, userRoles);
        log.info("User {} created", user.getUsername());
    }
}
