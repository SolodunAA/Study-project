package diary.app.dao;

import diary.app.dto.Token;

import java.util.Optional;

public interface TokenDao {
    void deleteToken(String user);
    void addToken(Token token);
    Optional<Token> getToken(String user);
}
