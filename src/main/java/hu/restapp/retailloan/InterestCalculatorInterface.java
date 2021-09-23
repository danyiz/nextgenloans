package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;

import java.math.BigDecimal;

public interface InterestCalculatorInterface {
    Interest calculateInterestForPeriod(RetailLoanAttributes retailLoanAttributes);
    Interest calculateDailyInterest(BigDecimal principalAmount, BigDecimal interestRate, Integer interestBase);
}
