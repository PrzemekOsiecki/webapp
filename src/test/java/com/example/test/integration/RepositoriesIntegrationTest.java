package com.example.test.integration;

import com.example.backend.persistence.domain.backend.Plan;
import com.example.backend.persistence.domain.backend.Role;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.backend.persistence.repositories.PlanRepository;
import com.example.backend.persistence.repositories.RoleRepository;
import com.example.backend.persistence.repositories.UserRepository;
import com.example.enums.PlansEnum;
import com.example.enums.RolesEnum;
import com.example.utils.UserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RepositoriesIntegrationTest {

    public static final int BASIC_ID = 1;

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Before
    public void init() {
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(roleRepository);
    }

    @Test
    public void shouldCreateNewPlan() throws Exception {
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Optional<Plan> retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void shouldCreateNewRole() {
        Role role = createRole(RolesEnum.BASIC);
        roleRepository.save(role);
        Optional<Role> retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void shouldCreateNewUser() {
        User basicUser = createUser();

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

    @Test
    public void shouldDeleteUser() {
        User user = createUser();
        userRepository.deleteById(user.getId());
    }

    private User createUser() {
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        userRoles.add(userRole);

//        basicUser.getUserRoles().addAll(userRoles);
        basicUser.setUserRoles(userRoles);
        basicUser = userRepository.save(basicUser);
        return basicUser;
    }




    private Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

    private Plan createBasicPlan(PlansEnum plansEnum) {
        return new Plan(plansEnum);
    }

}
