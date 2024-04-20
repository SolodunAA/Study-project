package diary.app.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static java.time.ZoneOffset.UTC;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditItem auditItem = (AuditItem) o;
        return timestamp == auditItem.timestamp && Objects.equals(user, auditItem.user) && Objects.equals(action, auditItem.action) && Objects.equals(userInput, auditItem.userInput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, timestamp, action, userInput);
    }

    @Override
    public String toString() {
        return "AuditItem{" +
                "user='" + user + '\'' +
                ", timestamp=" + LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), UTC) +
                ", action='" + action + '\'' +
                ", userInput='" + userInput + '\'' +
                '}';
    }
}
