package com.ronanvcjunior.taskmaster.events.listeners;

import com.ronanvcjunior.taskmaster.events.UserEvent;
import com.ronanvcjunior.taskmaster.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event) {
        switch (event.getType()) {
            case REGISTRATION -> emailService.sendNewAccountEmail(event.getUser().getFirstName() + " " + event.getUser().getLastName(), event.getUser().getEmail(), (String) event.getData().get("key"));
            case RESET_PASSWORD -> emailService.sendPasswordResetEmail(event.getUser().getFirstName() + " " + event.getUser().getLastName(), event.getUser().getEmail(), (String) event.getData().get("key"));
            default -> {}
        }
    }
}
