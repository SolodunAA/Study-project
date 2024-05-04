package diary.app.service;

import diary.app.dto.Token;

/**
 * interaction with token
 */
public interface TokenService {
    /**
     * creating token for this user
     * @param user user login
     * @return token
     */
    Token createToken(String user);

    /**
     * used for checking token
     * @param user user login
     * @param token token
     * @return is expired or not
     */
    boolean validateToken(String user, String token);
}
