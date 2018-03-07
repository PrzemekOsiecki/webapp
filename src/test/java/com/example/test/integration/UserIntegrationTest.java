package com.example.test.integration;

import com.example.WebappApplication;
import com.example.backend.persistence.domain.backend.Plan;
import com.example.backend.persistence.domain.backend.Role;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.config.ApplicationConfiguration;
import com.example.config.DevelopmentConfig;
import com.example.enums.PlansEnum;
import com.example.enums.RolesEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WebappApplication.class, ApplicationConfiguration.class, DevelopmentConfig.class})
public class UserIntegrationTest extends AbstractIntegrationTest {

    public static final int BASIC_ID = 1;

    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(roleRepository);
    }

    @Rule
    public TestName testName = new TestName();

    @Test
    public void shouldCreateNewPlan() throws Exception {
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void shouldCreateNewRole() throws Exception {
        Role role = createRole(RolesEnum.BASIC);
        roleRepository.save(role);
        Optional<Role> retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void shouldCreateNewUser() throws Exception {

        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@dev.com";

        User basicUser = createUser(username, email);

        //when
        User newlyCreatedUser = userRepository.findById((long) BASIC_ID).get();

        //then
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.getPlan());
        Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
        Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.getUserRoles();
        newlyCreatedUserUserRoles.forEach((ur) -> {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        });
    }

    //@Test
    public void shouldDeleteUser() throws Exception {
        String username = testName.getMethodName();
        String email = testName.getMethodName() + "@dev.com";

        User user = createUser(username, email);
        userRepository.deleteById(user.getId());
    }

    @Test
    public void shouldUpdateUserPassword() {
        User user = createUser(testName);

        String newPassword = UUID.randomUUID().toString();

        userRepository.updateUserPassword(user.getId(), newPassword);

        Optional<User> retrievedUser = userRepository.findById(user.getId());
        Assert.assertEquals(newPassword, retrievedUser.get().getPassword());
    }
}
