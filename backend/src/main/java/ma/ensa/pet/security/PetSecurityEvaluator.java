package ma.ensa.pet.security;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.model.User;
import ma.ensa.pet.service.PetService;
import ma.ensa.pet.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("petSecurity")
@RequiredArgsConstructor
public class PetSecurityEvaluator {
    private final PetService petService;
    private final UserService userService;

    public boolean isOwner(Long petId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        User user = userService.getUserByEmail(currentUserEmail);
        Pet pet = petService.getPetById(petId);

        return pet.getOwner().getId().equals(user.getId());
    }
}