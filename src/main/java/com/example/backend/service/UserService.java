package com.example.backend.service;

import com.example.backend.persistence.domain.backend.Plan;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.backend.persistence.repositories.PlanRepository;
import com.example.backend.persistence.repositories.RoleRepository;
import com.example.backend.persistence.repositories.UserRepository;
import com.example.enums.PlansEnum;
import com.example.enums.RolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class UserService {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {
        Plan plan = new Plan(plansEnum);
        if (!planRepository.existsById(plansEnum.getId())) {
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);
        userRoles.forEach((ur) -> roleRepository.save(ur.getRole()));

        user.setUserRoles(userRoles);
        user = userRepository.save(user);
        return user;
    }
}
