package hu.restapp.controller;

import hu.restapp.retailloan.RetailLoanCalculator;
import hu.restapp.retailloan.model.RetailLoanAttributes;
import hu.restapp.retailloan.model.RetailLoanScheduleDTO;
import hu.restapp.systemutility.SystemFrequency;
import hu.restapp.systemutility.TriggerDateUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
        //return new TriggerDate().getNextDateFromDate(localDate, SystemFrequency.MONTHLY,26).toString();
    }

     @GetMapping("/calcdays")
    public String calcDays() {
        log.info("CalcDate is called");
        return new TriggerDateUtility().calcDaysBetweenDates(LocalDate.now(),LocalDate.now().minusDays(200)).toString();
    }

     @GetMapping("/calcloan")
    public List<RetailLoanScheduleDTO> calcLoan() {
        log.info("CalcLoan is called");
        BigDecimal intrate = new BigDecimal("27.90000000");
        Integer numOfPAy = 18;
        BigDecimal loanAmount = new BigDecimal("328500.00000000") ;
        BigDecimal subsidizedRate = new BigDecimal("0.00000000") ;
        BigDecimal bonusRate = new BigDecimal("0.00000000") ;
        RetailLoanAttributes retailLoanAttributes = new RetailLoanAttributes();
        retailLoanAttributes.setTermsToDelayPrincipalPayment(0);
        retailLoanAttributes.setNextPaymentNumber(1);
        retailLoanAttributes.setAccountNumber("1111111111111111111111111");
        retailLoanAttributes.setCalculationBasis(360);
        retailLoanAttributes.setLoanPrincipalAmount(loanAmount);
        retailLoanAttributes.setLoanInterestRate(intrate);
        retailLoanAttributes.setNumberOfPayments(numOfPAy);
        retailLoanAttributes.setDaysInPeriod(30);
        retailLoanAttributes.setCorrectionCoefficient(new BigDecimal("1.013889"));
        retailLoanAttributes.setCurrencyCode("HUF");
        retailLoanAttributes.setPayDay(12);
        retailLoanAttributes.setSettlementFrequencyInMoths(1);
        retailLoanAttributes.setPrincipalPart(new BigDecimal("0"));

        return retailLoanCalculator.generateSchedule(true,retailLoanAttributes);
    }
}