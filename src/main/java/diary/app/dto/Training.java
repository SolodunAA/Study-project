package diary.app.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Training {
    private final LocalDate date;
    private final String type;
    private final double timeInMinutes;
    private final int calories;
    private final String additionalInfo;

    public Training(LocalDate date, String type, double timeInMinutes,
                    int calories, String additionalInfo){
        this.date = date;
        this.type = type;
        this.timeInMinutes = timeInMinutes;
        this.calories = calories;
        this.additionalInfo = additionalInfo;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double getTimeInMinutes() {
        return timeInMinutes;
    }

    public int getCalories() {
        return calories;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public String toString() {
        return "|| " + date +
                " | " + type +
                " | " + timeInMinutes +
                " | " + calories +
                " | " + additionalInfo + " || ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return Double.compare(timeInMinutes, training.timeInMinutes) == 0 && calories == training.calories && Objects.equals(date, training.date) && Objects.equals(type, training.type) && Objects.equals(additionalInfo, training.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type, timeInMinutes, calories, additionalInfo);
    }
}
