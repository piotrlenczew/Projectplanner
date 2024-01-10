package pw.pap.api.dto;

import pw.pap.model.User;

public class AuthResponseDTO {
    private String token;
    private User user;

    public AuthResponseDTO(String jwtToken, User user) {
        this.token = jwtToken;
        this.user = user;
    }

    public String getJwtToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}