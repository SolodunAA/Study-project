package diary.app.dto;

import java.util.Objects;

public class Token {
    private final String login;
    private final String token;
    private final long timestamp;

    public Token(String login, String token, long timestamp) {
        this.login = login;
        this.token = token;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return timestamp == token1.timestamp && Objects.equals(login, token1.login) && Objects.equals(token, token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, token, timestamp);
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
