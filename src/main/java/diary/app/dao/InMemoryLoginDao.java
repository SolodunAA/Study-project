package diary.app.dao;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLoginDao implements LoginDao {

    private final Map<String, Integer> pswdCache = new HashMap<>();

    @Override
    public boolean checkIfUserExist(String login) {

        return pswdCache.containsKey(login);
    }

    @Override
    public void addNewUser(String username, int encodedPswd) {
        pswdCache.put(username, encodedPswd);
    }

    @Override
    public int getEncodedPassword(String login) {

        return pswdCache.get(login);
    }
}
