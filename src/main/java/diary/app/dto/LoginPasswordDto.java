package diary.app.dto;

public class LoginPasswordDto {
    private String login;
    private String password;

    public LoginPasswordDto() {

    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
