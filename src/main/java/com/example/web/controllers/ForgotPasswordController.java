package com.example.web.controllers;

import com.example.backend.persistence.domain.backend.PasswordResetToken;
import com.example.backend.persistence.domain.backend.User;
import com.example.backend.service.EmailService;
import com.example.backend.service.PasswordResetTokenService;
import com.example.backend.service.UserService;
import com.example.utils.UserUtils;
import com.example.web.i18n.I18NService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

@Controller
@Slf4j
public class ForgotPasswordController {

    private static final String EMAIL_ADDRESS_VIEW_NAME = "forgotpassword/emailForm";
    private static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotpassword";
    private static final String MAIL_SENT_KEY = "mailSent";

    public static final String CHANGE_PASSWORD_PATH = "/changepassword";

    private static final String FORGOT_PASSWORD_EMAIL_TEXT = "forgotpassword.mail.text";
    private static final String CHANGE_PASSWORD_VIEW_NAME = "forgotpassword/changePassword";
    public static final String PASSWORD_RESET_PARAMETER_NAME = "passwordReset";
    public static final String MESSAGE_PARAMETER_NAME = "message";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private UserService userService;

    @Value("${webmaster.email}")
    private String webmasterEmail;

    @Autowired
    EmailService emailService;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    String forgotPassword() {
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    String resetPassword(HttpServletRequest httpServletRequest,
                         @RequestParam("email") String email,
                         ModelMap modelMap) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

        if(passwordResetToken == null) {
            log.warn("Could not find user with given email {}", email);
        } else {
            log.warn("Token value: {}", passwordResetToken.getToken());
            log.warn("username {}", passwordResetToken.getUser().getUsername());


            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();

            String resetPasswordUrl = UserUtils.createPasswordResetUrl(httpServletRequest, user.getId(), token);
            log.debug("Reset Password Url {}", resetPasswordUrl);

            String emailText = i18NService.getMessage(FORGOT_PASSWORD_EMAIL_TEXT, httpServletRequest.getLocale());

            SimpleMailMessage mailMessage = createResetPasswordEmailMessage(user, resetPasswordUrl, emailText);

            emailService.sendGenericEmailMessage(mailMessage);
        }

        modelMap.addAttribute(MAIL_SENT_KEY, "true");
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    String changeUserPasswordGet(@RequestParam("id") long id,
                              @RequestParam("token") String token,
                              Locale locale,
                              ModelMap model) { //Spring will automatically fill Locale and ModelMap

        if(StringUtils.isEmpty(token) || id == 0) {
            log.error("Invalid user id {} or token value {}", id, token);
            model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "false");
            model.addAttribute(MESSAGE_PARAMETER_NAME, "Invalid user id or token");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

        if(passwordResetToken == null) {
            log.warn("A token could not be found with value {}", token);
            model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "false");
            model.addAttribute(MESSAGE_PARAMETER_NAME, "Token not found");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.getUser();

        if(user.getId() != id) {
            log.error("The user id {} passed as a parameter does not match the user id {} associated with the token {}",
                    id, user.getId(), token);
            model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "false");
            model.addAttribute(MESSAGE_PARAMETER_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if(LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())) {
            model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "false");
            model.addAttribute(MESSAGE_PARAMETER_NAME,i18NService.getMessage("resetPassword.token.expired"));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        model.addAttribute("principalId", user.getId());

        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    String changeUserPasswordPost(@RequestParam("principalId") long userId,
                                         @RequestParam("password") String password,
                                         ModelMap model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            log.error("an unauthenticated user tried to invoke the reset password POST method");
            model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "false");
            model.addAttribute(MESSAGE_PARAMETER_NAME, "You are not authorized to perform this request");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) authentication.getPrincipal();
        if (user.getId() != userId) {
            log.error("Security breach! User {} is trying to make password reset request on behalf of {}",
                    user.getId(), userId);
            model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "false");
            model.addAttribute(MESSAGE_PARAMETER_NAME, "You are not authorized to perform this request");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(userId, password);
        log.info("Password successfully updated for user {}", user.getUsername());

        model.addAttribute(PASSWORD_RESET_PARAMETER_NAME, "true");
        return CHANGE_PASSWORD_VIEW_NAME;
    }

    private SimpleMailMessage createResetPasswordEmailMessage(User user, String resetPasswordUrl, String emailText) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("[DevApp] Reset Password Email");
        mailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
        mailMessage.setFrom(webmasterEmail);
        return mailMessage;
    }
}
