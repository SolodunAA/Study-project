package diary.app.service.Impl;

import diary.app.dao.TokenDao;
import diary.app.dto.Token;

import java.util.UUID;

public class TokenServiceImpl {
    private final long lifeTime = 10000000;
    private final TokenDao tokenDao;

    public TokenServiceImpl(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public Token createToken(String user) {
        String token = UUID.randomUUID().toString();
        tokenDao.deleteToken(user);
        Token tokenDto = new Token(user, token, System.currentTimeMillis());
        tokenDao.addToken(tokenDto);
        return tokenDto;
    }

    public boolean validateToken(String user, String token) {
        return tokenDao.getToken(user)
                .filter(dto -> System.currentTimeMillis() - dto.getTimestamp() < lifeTime)
                .map(Token::getToken)
                .filter(token::equals)
                .isPresent();
    }
}
