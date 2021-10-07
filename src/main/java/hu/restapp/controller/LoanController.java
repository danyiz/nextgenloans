package hu.restapp.controller;

import hu.restapp.retailloan.RetailLoanCalculator;
import hu.restapp.retailloan.model.*;
import hu.restapp.systemutility.SystemFrequency;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class LoanController {

    @Autowired
    CurrencyDAO currencyDAO;

    @Autowired
    RetailLoanCalculator retailLoanCalculator;
    @GetMapping("/getsystemdate")
    public String getSystemDate() {
        log.info("Sysdate is called");
        return new TriggerDateUtility().getSystemDate().toString();
    }

     @GetMapping("/getnextdate")
    public String getNextDate() {
        log.info("Next date is called");
        LocalDate localDate = LocalDate.now();
        return new TriggerDateUtility().calculateNextDateFromDate(localDate, SystemFrequency.YEARLY,18).toString();
    }

     @GetMapping("/calcdays")
    public String calcDays() {
        log.info("CalcDate is called");
        return new TriggerDateUtility().calcDaysBetweenDates(LocalDate.now(),LocalDate.now().minusDays(200)).toString();
    }

     @GetMapping("/calcloan")
         public List<RetailLoanScheduleDTO> calcLoan( @RequestHeader RetailLoanAttributes retailLoanAttributes) {
        log.info("CalcLoan is called");
        try {
             Currency currency = currencyDAO.getCurrency(retailLoanAttributes.getCurrencyCode());
             retailLoanAttributes.setCurrencyDecimals(currency.getCurrencyDecimals());
        } catch (CurrencyNotExistsException e) {
             e.printStackTrace();
        }

        return retailLoanCalculator.generateSchedule(true,retailLoanAttributes);
    }
}