package diary.app.dto;

public class AuditItem {
    private final String user;
    private final long timestamp;
    private final String action;
    private final String userInput;

    public AuditItem(String user, String action, String userInput) {
        this.user = user;
        this.timestamp = System.currentTimeMillis();
        this.action = action;
        this.userInput = userInput;
    }

    @Override
    public String toString() {
        return "AuditItem{" +
                "user='" + user + '\'' +
                ", timestamp=" + timestamp +
                ", action='" + action + '\'' +
                ", userInput='" + userInput + '\'' +
                '}';
    }
}
