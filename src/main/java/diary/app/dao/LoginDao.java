package diary.app.dao;

public interface LoginDao {

    boolean checkIfUserExist(String login);
    void addNewUser(String login, int encodedPswd);
    int getEncodedPassword(String login);

}
