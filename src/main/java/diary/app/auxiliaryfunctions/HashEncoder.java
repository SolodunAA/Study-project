package diary.app.auxiliaryfunctions;

public class HashEncoder implements PasswordEncoder {

    @Override
    public int encode(String pswd) {
        return pswd.hashCode();
    }
}
