package hu.restapp.controller;

import hu.restapp.retailloan.RetailLoanCalculator;
import hu.restapp.retailloan.model.RetailLoanAttributes;
import hu.restapp.retailloan.model.RetailLoanScheduleDTO;
import hu.restapp.systemutility.SystemFrequency;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class LoanController {

    @Autowired
    RetailLoanCalculator retailLoanCalculator;
    @RequestMapping("/getsystemdate")
    public String getSystemDate() {
        log.info("Sysdate is called");
        return new TriggerDateUtility().getSystemDate().toString();
    }

    @RequestMapping("/getnextdate")
    public String getNextDate() {
        log.info("Next date is called");
        LocalDate localDate = LocalDate.now();
        return new TriggerDateUtility().getNextDateFromDate(localDate, SystemFrequency.YEARLY,18).toString();
        //return new TriggerDate().getNextDateFromDate(localDate, SystemFrequency.MONTHLY,26).toString();
    }

    @RequestMapping("/calcdays")
    public String calcDays() {
        log.info("CalcDate is called");
        return new TriggerDateUtility().calcDaysBetweenDates(LocalDate.now(),LocalDate.now().minusDays(200)).toString();
    }

    @RequestMapping("/calcloan")
    public List<RetailLoanScheduleDTO> calcLoan() {
        log.info("CalcLoan is called");
        BigDecimal intrate = new BigDecimal("27.90000000");
        intrate.setScale(8, RoundingMode.HALF_EVEN);
        Integer numOfPAy = 18;
        BigDecimal loanAmount = new BigDecimal("328500.00000000") ;
        loanAmount.setScale(8, RoundingMode.HALF_EVEN);
        BigDecimal subsidizedRate = new BigDecimal("0.00000000") ;
        BigDecimal bonusRate = new BigDecimal("0.00000000") ;
        RetailLoanAttributes retailLoanAttributes = new RetailLoanAttributes();
        retailLoanAttributes.setCalculationBasis(360);
        retailLoanAttributes.setLoanPrincipalAmount(loanAmount);
        retailLoanAttributes.setLoanInterestRate(intrate);
        retailLoanAttributes.setNumberOfPayments(numOfPAy);
        retailLoanAttributes.setDaysInPeriod(Integer.valueOf(30));
        retailLoanAttributes.setCorrectionCoefficient(BigDecimal.valueOf(1L));
        retailLoanCalculator.generateSchedule(true,retailLoanAttributes);
        return retailLoanCalculator.getLoanShed();
    }
}