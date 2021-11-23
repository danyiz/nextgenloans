package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NewAnnuityInterestCalculator implements InterestCalculatorInterface{
    @Override
    public Interest calculateInterestForPeriod(RetailLoanAttributes retailLoanAttributes) {
        //Ip= repayment *(1-(1+Rate)^p-1/(1+Rate)^NPeriods)
        BigDecimal periodInterestRate = retailLoanAttributes.getLoanInterestRate().divide(BigDecimal.valueOf(1200L),8,RoundingMode.DOWN).multiply(retailLoanAttributes.getCorrectionCoefficient());
        BigDecimal periodInterestRatePlusOne = periodInterestRate.add(new BigDecimal("1"));
        BigDecimal multi1 = BigDecimal.valueOf(Math.pow(periodInterestRatePlusOne.doubleValue(), (double)retailLoanAttributes.getNextPaymentNumber()-1 ));
        BigDecimal multi2 = BigDecimal.valueOf(Math.pow(periodInterestRatePlusOne.doubleValue(), (double)retailLoanAttributes.getNumberOfPayments() )) ;
        BigDecimal multi = multi1.divide(multi2,8,RoundingMode.DOWN);
        multi = new BigDecimal("1").subtract(multi);
        BigDecimal interestFraction = retailLoanAttributes.getRegularPayment().multiply(multi);
        retailLoanAttributes.setInterestPart(interestFraction);
        Interest interest =  new Interest();
        interest.setInterestAmount(retailLoanAttributes.getInterestPart());
        return interest;
    }

    @Override
    public Interest calculateDailyInterest(BigDecimal principalAmount, BigDecimal interestRate, Integer interestBase) {
        return null;
    }
}
