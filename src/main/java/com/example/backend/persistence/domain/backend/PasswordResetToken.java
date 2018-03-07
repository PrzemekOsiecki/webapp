package com.example.backend.persistence.domain.backend;

import com.example.backend.persistence.converters.LocalDateTimeAttributeConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(exclude = {"token", "user", "expiryDate"})
@Slf4j
public class PasswordResetToken implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_TOKEN_EXPIRING_TIME = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, User user, LocalDateTime creationDateTime, int expirationInMinutes) {
        if((token == null) || (user == null) || (creationDateTime == null)) {
            throw new IllegalArgumentException("token, user and creationDateTime cannot be null");
        }
        if(expirationInMinutes == 0) {
            log.warn("The token expiration time is equal 0, assigning default value {}", DEFAULT_TOKEN_EXPIRING_TIME);
            expirationInMinutes = DEFAULT_TOKEN_EXPIRING_TIME;
        }
        this.token = token;
        this.user = user;
        expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
    }
}
