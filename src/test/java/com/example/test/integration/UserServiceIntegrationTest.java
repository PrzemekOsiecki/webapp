package com.example.test.integration;

import com.example.WebappApplication;
import com.example.backend.persistence.domain.backend.Role;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.config.ApplicationConfiguration;
import com.example.config.DevelopmentConfig;
import com.example.enums.PlansEnum;
import com.example.enums.RolesEnum;
import com.example.utils.UserUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WebappApplication.class, ApplicationConfiguration.class, DevelopmentConfig.class})
public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Rule
    public TestName testName = new TestName();

    @Test
    public void shouldCreateNewUser() {
        //given
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "dev.com";
        Set<UserRole> userRoles = new HashSet<>();
        User basicUser = UserUtils.createBasicUser(username, email);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
        //when
        User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
        //then
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
    }
}
