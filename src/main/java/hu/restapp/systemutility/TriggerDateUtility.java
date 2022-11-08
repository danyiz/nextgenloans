package hu.restapp.systemutility;

import lombok.extern.slf4j.Slf4j;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.validate.ValidationException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Objects;

@Component
@Slf4j
public  class TriggerDateUtility {


    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = DEFAULT_DATE_FORMAT + " HH:mm:ss";
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern(DEFAULT_DATETIME_FORMAT)
            .toFormatter();
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = new DateTimeFormatterBuilder().appendPattern(DEFAULT_DATE_FORMAT)
            .toFormatter();
    public static LocalDate getSystemDate() {
        return  LocalDate.now();
    }


    public static LocalDate calculateNextDateFromDate(LocalDate fromDate, SystemFrequency frequency, Integer day) {
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


    public static Long calcDaysBetweenDates(LocalDate fromDate, LocalDate toDate) {
        return toDate.toEpochDay() - fromDate.toEpochDay();
    }


    public static LocalDate calcNextDate(Integer plusMonths, LocalDate fromDate, Integer day){

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
    public static Recur getICalRecur(final String recurringRule) {

        // Construct RRule
        try {
            final RRule rrule = new RRule(recurringRule);
            rrule.validate();
            return rrule.getRecur();
        } catch (final ParseException e) {
            // TODO Auto-generated catch block
            log.error("Problem occurred in getICalRecur function", e);
        } catch (final ValidationException e) {
            // TODO Auto-generated catch block
            log.error("Problem occurred in getICalRecur function validation", e);
        }

        return null;
    }
    private static LocalDateTime getNextRecurringDate(final Recur recur, final LocalDateTime seedDate, final LocalDateTime startDate) {
        final DateTime periodStart = new DateTime(java.util.Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
        final Date seed = convertToiCal4JCompatibleDate(seedDate);
        final Date nextRecDate = recur.getNextDate(seed, periodStart);
        return nextRecDate == null ? null : LocalDateTime.ofInstant(nextRecDate.toInstant(),ZoneId.systemDefault());
    }

    public static LocalDate getNextRecurringDate(final Recur recur, final LocalDate seedDate, final LocalDate startDate) {
        final DateTime periodStart = new DateTime(java.util.Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        final Date seed = convertToiCal4JCompatibleDate(seedDate.atStartOfDay());
        final Date nextRecDate = recur.getNextDate(seed, periodStart);
        return nextRecDate == null ? null : LocalDate.ofInstant(nextRecDate.toInstant(), ZoneId.systemDefault());
    }

    public static Date convertToiCal4JCompatibleDate(final LocalDateTime inputDate) {
        Date formattedDate = null;
        final String seedDateStr = DEFAULT_DATETIME_FORMATTER.format(inputDate);
        try {
            formattedDate = new Date(seedDateStr, DEFAULT_DATETIME_FORMAT);
        } catch (final ParseException e) {
            log.error("Invalid date: {}", seedDateStr, e);
        }
        return formattedDate;
    }

    public static LocalDateTime getNextRecurringDate(final String recurringRule, final LocalDateTime seedDate,
                                                     final LocalDateTime startDate) {
        final Recur recur = getICalRecur(recurringRule);
        if (recur == null) {
            return null;
        }

        //TODO
        // nextDate = (LocalDateTime) adjustDate(nextDate, seedDate, getMeetingPeriodFrequencyType(recurringRule));
       return getNextRecurringDate(recur, seedDate, startDate);

    }

    public static LocalDate getNextRecurringDate(final String recurringRule, final LocalDate seedDate, final LocalDate startDate) {
        final Recur recur = getICalRecur(recurringRule);
        if (recur == null) {
            return null;
        }
        LocalDate nextDate = getNextRecurringDate(recur, seedDate, startDate);
        // nextDate = (LocalDate) adjustDate(nextDate, seedDate, getMeetingPeriodFrequencyType(recurringRule));
        return nextDate;
    }

}