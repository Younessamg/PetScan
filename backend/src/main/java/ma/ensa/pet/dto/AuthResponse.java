package ma.ensa.pet.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String message;

    public AuthResponse(String message, Long userId, String token) {
        this.message = message;
        this.userId = userId;
        this.token = token;
    }
}