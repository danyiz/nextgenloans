package hu.restapp.retailloan;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BasicLoanInterestCalculator implements InterestCalculatorInterface {

    @Override
    public Interest calculateInterestForPeriod(BigDecimal principalAmount, BigDecimal interestRate, Long daysInPeriod,Integer interestBase) {
        Interest interestForPeriod = new Interest();
        interestForPeriod.setInterestAmount(calculateDailyInterest(principalAmount, interestRate, interestBase).getInterestAmount().multiply(BigDecimal.valueOf(daysInPeriod)));
        return interestForPeriod;
    }

    @Override
    public Interest calculateDailyInterest(BigDecimal principalAmount, BigDecimal interestRate, Integer interestBase) {
        Interest dailyInterest = new Interest();
        dailyInterest.setInterestAmount(principalAmount.multiply(interestRate.divide(BigDecimal.valueOf(100L))).divide(BigDecimal.valueOf((long)interestBase),8, RoundingMode.HALF_EVEN));
        return dailyInterest;
    }
}
