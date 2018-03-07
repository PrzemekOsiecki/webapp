package com.example.backend.service;

import com.example.backend.persistence.domain.backend.Plan;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.domain.backend.UserRole;
import com.example.backend.persistence.repositories.PlanRepository;
import com.example.backend.persistence.repositories.RoleRepository;
import com.example.backend.persistence.repositories.UserRepository;
import com.example.enums.PlansEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles) {

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        Plan plan = new Plan(plansEnum);
        if (!planRepository.existsById(plansEnum.getId())) {
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);
        userRoles.forEach((ur) -> roleRepository.save(ur.getRole()));

        user.setUserRoles(userRoles);
        //user.getUserRoles().addAll(userRoles);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    public void updateUserPassword(long userId, String password) {
        password = bCryptPasswordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        log.debug("Password successfully updated for user with id: {}", userId);
    }
}
