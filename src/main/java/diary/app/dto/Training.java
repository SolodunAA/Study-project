package diary.app.dto;

import java.time.LocalDate;

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
}
