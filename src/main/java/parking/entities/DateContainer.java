package parking.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class DateContainer {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTime;

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }
}
