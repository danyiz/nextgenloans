package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BasicLoanInterestCalculator implements InterestCalculatorInterface {

    @Override
    public Interest calculateInterestForPeriod(RetailLoanAttributes retailLoanAttributes) {
        Interest interestForPeriod = new Interest();
        interestForPeriod.setInterestAmount(calculateDailyInterest(retailLoanAttributes.getLoanPrincipalAmount(), retailLoanAttributes.getLoanInterestRate(),retailLoanAttributes.getCalculationBasis()).getInterestAmount().multiply(BigDecimal.valueOf(retailLoanAttributes.getDaysInPeriod())));
        return interestForPeriod;
    }

    @Override
    public Interest calculateDailyInterest(BigDecimal principalAmount, BigDecimal interestRate, Integer interestBase) {
        Interest dailyInterest = new Interest();
        dailyInterest.setInterestAmount(principalAmount.multiply(interestRate.divide(BigDecimal.valueOf(100L))).divide(BigDecimal.valueOf((long)interestBase),8, RoundingMode.HALF_EVEN));
        dailyInterest.setInterestInteger(dailyInterest.getInterestAmount().divide(BigDecimal.valueOf(1L),2,RoundingMode.HALF_UP));
        dailyInterest.setInterestWash(dailyInterest.getInterestAmount().subtract(dailyInterest.getInterestInteger()));
        return dailyInterest;
    }
}
