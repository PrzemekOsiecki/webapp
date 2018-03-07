package com.example.backend.service;

import com.example.backend.persistence.domain.backend.PasswordResetToken;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.persistence.repositories.PasswordResetTokenRepository;
import com.example.backend.persistence.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PasswordResetTokenService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${token.expiration.time.minutes}")
    private int tokenExpirationInMinutes;

    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public PasswordResetToken createPasswordResetTokenForEmail(String email) {
        PasswordResetToken passwordResetToken = null;
        User user = userRepository.findByEmail(email);

        if(user != null) {
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordResetToken = new PasswordResetToken(token, user, now, tokenExpirationInMinutes);

            passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
        } else {
            log.warn("We could not find a user for given email {}", email);
        }

        return passwordResetToken;
    }
}
