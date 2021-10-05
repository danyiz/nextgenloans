package hu.restapp.systemutility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class TriggerDateUtility implements DateUtility {

    @Override
    public LocalDate getSystemDate() {
        return  LocalDate.now();
    }

    @Override
    public LocalDate calculateNextDateFromDate(LocalDate fromDate, SystemFrequency frequency, Integer day) {
        if(Objects.isNull(fromDate)) fromDate = LocalDate.now();
        LocalDate localDate = fromDate;
        switch(frequency) {
            case MONTHLY:
                localDate = calcNextDate(1,fromDate,day);
                break;
            case QUARTERLY:
                localDate = calcNextDate(3,fromDate,day);
                break;
            case HALFYEARLY:
                localDate = calcNextDate(6,fromDate,day);
                break;
            case YEARLY:
                localDate = calcNextDate(12,fromDate,day);
                break;
            // to implement
            //every day
            //every nn day
            //every nn working day
            //weekly on specified day 1 Monday 7 Sunday
            // penultimate working day in month/quarterly/halfyear/year
            // last working day in month/quarterly/halfyear/year
            // penultimate calendar day in month/quarterly/halfyear/year
            //last calendar day in month/quarterly/halfyear/year
            // every n th day 1 Monday 7 Sunday in month/quarterly/halfyear/year from month

        }
        return localDate;
    }

    @Override
    public Long calcDaysBetweenDates(LocalDate fromDate, LocalDate toDate) {
        return toDate.toEpochDay() - fromDate.toEpochDay();
    }

    @Override
    public LocalDate calcNextDate(Integer plusMonths, LocalDate fromDate, Integer day){

        int currentDay = fromDate.getDayOfMonth();
        if (currentDay<day && plusMonths>=1) {
            if(fromDate.getMonth().getValue() == 2 && (fromDate.getDayOfMonth() != 28)) {
                plusMonths = plusMonths - 1;
            }
        }
        int dayInternal = day;
        if(fromDate.getMonth().getValue() == 1 && (dayInternal == 29 || dayInternal == 30 || dayInternal == 31)){
            dayInternal = 28;
            fromDate = fromDate.minusDays(day-28);
        }

        LocalDate localDate = fromDate.plusMonths(plusMonths);
        int  month = localDate.getMonth().getValue();
        int year = localDate.getYear();

        return LocalDate.of(year,month,dayInternal);
    }
}
