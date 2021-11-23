package hu.restapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.restapp.retailloan.RetailLoanCalculator;
import hu.restapp.retailloan.model.*;
import hu.restapp.systemutility.SystemFrequency;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class LoanController {

    @Autowired
    CurrencyDAO currencyDAO;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RetailLoanCalculator retailLoanCalculator;

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

     @PostMapping("/calcloan")
     public ResponseEntity<List<RetailLoanScheduleDTO>> calcLoan(@RequestBody RetailLoanAttributes retailLoanAttributes) throws IOException {

        log.info("CalcLoan is called");
        try {
             Currency currency = currencyDAO.getCurrency(retailLoanAttributes.getCurrencyCode());
             retailLoanAttributes.setCurrencyDecimals(currency.getCurrencyDecimals());
        } catch (CurrencyNotExistsException e) {
             e.printStackTrace();
        }

        return new ResponseEntity<List<RetailLoanScheduleDTO>>(retailLoanCalculator.generateSchedule(true,retailLoanAttributes), HttpStatus.OK);
    }
}