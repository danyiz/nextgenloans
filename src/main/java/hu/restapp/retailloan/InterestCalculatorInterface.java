package hu.restapp.retailloan;

import java.math.BigDecimal;

public interface InterestCalculatorInterface {
    Interest calculateInterestForPeriod(BigDecimal principalAmount, BigDecimal interestRate,Long daysInPeriod,Integer interestBase);
    Interest calculateDailyInterest(BigDecimal principalAmount, BigDecimal interestRate, Integer interestBase);
}
