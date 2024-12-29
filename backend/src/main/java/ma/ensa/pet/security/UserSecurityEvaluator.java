package ma.ensa.pet.security;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.User;
import ma.ensa.pet.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurityEvaluator {
    private final UserService userService;

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        User user = userService.getUserByEmail(currentUserEmail);
        return user.getId().equals(userId);
    }
}