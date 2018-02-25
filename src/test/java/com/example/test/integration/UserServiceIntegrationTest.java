package com.example.test.integration;

import com.example.backend.persistence.domain.backend.Role;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.backend.service.UserService;
import com.example.enums.PlansEnum;
import com.example.enums.RolesEnum;
import com.example.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    public void shouldCreateNewUser() {
        //given
        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser();
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
        //when
        User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
        //then
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
    }
}
