package diary.app.dao.inmemory;

import diary.app.dao.LoginDao;

import java.util.HashMap;
import java.util.Map;

/**
 * class stores logins and passwords
 */
public class InMemoryLoginDao implements LoginDao {
    /**
     * map stores user login and his password
     */
    private final Map<String, Integer> pswdCache = new HashMap<>();

    /**
     * check has this user already registered in the system
     * @param login users login
     * @return if user exist in cache - true, else - false
     */
    @Override
    public boolean checkIfUserExist(String login) {

        return pswdCache.containsKey(login);
    }

    /**
     * add new user to the system
     * @param login registered user
     * @param encodedPswd encode password of this user
     */
    @Override
    public void addNewUser(String login, int encodedPswd) {
        pswdCache.put(login, encodedPswd);
    }

    /**
     * get encoded password
     * @param login - user login
     * @return encoded password
     */

    @Override
    public int getEncodedPassword(String login) {

        return pswdCache.get(login);
    }
}
