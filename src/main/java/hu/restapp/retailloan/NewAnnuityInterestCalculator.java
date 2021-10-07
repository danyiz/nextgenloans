package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;

import java.math.BigDecimal;

public class NewAnnuityInterestCalculator implements InterestCalculatorInterface{
    @Override
    public Interest calculateInterestForPeriod(RetailLoanAttributes retailLoanAttributes) {
        double perIntRate = ((double)retailLoanAttributes.getLoanInterestRate().longValue()/100/12 * (double)retailLoanAttributes.getCorrectionCoefficient().longValue()) +1;
        double multi = 1-(double)Math.pow((double)perIntRate, (double)retailLoanAttributes.getNextPaymentNumber() ) /(double)Math.pow((double)perIntRate, (double)retailLoanAttributes.getNumberOfPayments() ) ;
        double IntFrac = retailLoanAttributes.getRegularPayment().longValue() * multi;
        retailLoanAttributes.setInterestPart(new BigDecimal(IntFrac));
        Interest interest =  new Interest();
        interest.setInterestAmount(retailLoanAttributes.getInterestPart());
        return interest;
    }

    @Override
    public Interest calculateDailyInterest(BigDecimal principalAmount, BigDecimal interestRate, Integer interestBase) {
        return null;
    }
}
