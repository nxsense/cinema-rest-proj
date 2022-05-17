package cinema;

import java.util.UUID;

public class Token {
    UUID token;

    public Token(UUID token) {
        this.token = token;
    }

    public Token() {
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
