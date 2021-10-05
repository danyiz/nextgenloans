package hu.restapp.systemutility;

import java.time.LocalDate;

public interface DateUtility {
    LocalDate calcNextDate(Integer plusMonths, LocalDate fromDate, Integer day);
    LocalDate getSystemDate();
    LocalDate calculateNextDateFromDate(LocalDate fromDate, SystemFrequency frequency, Integer day);
    Long calcDaysBetweenDates(LocalDate fromDate, LocalDate toDate);

}

