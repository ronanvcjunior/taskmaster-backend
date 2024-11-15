package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.ronanvcjunior.taskmaster.utils.EmailUtils.getEmailMessage;
import static com.ronanvcjunior.taskmaster.utils.EmailUtils.getResetPasswordMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UNABLE_TO_SEND_EMAIL = "Unable to send email";
    public static final String PASSWORD_RESET_REQUEST = "Password Reset Request";

    private final JavaMailSender sender;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String to, String key) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, key));

            sender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException(UNABLE_TO_SEND_EMAIL);
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String to, String key) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getResetPasswordMessage(name, host, key));

            sender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException(UNABLE_TO_SEND_EMAIL);
        }
    }
}
