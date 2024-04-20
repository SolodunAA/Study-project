package diary.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public AuditItem(String user, long timestamp, String action, String userInput) {
        this.user = user;
        this.timestamp = timestamp;
        this.action = action;
        this.userInput = userInput;
    }

    public String getUser() {
        return user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }

    public String getUserInput() {
        return userInput;
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
