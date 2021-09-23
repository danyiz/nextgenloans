package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BasicRegularPaymentCalculator implements RegularPaymentCalculatorInterface {
    @Override
    public BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes) {
            //The formula P = iA / 1 - (1+i)^-N
        BigDecimal perIntRate = retailLoanAttributes.getLoanInterestRate().divide(BigDecimal.valueOf(1200L),8, RoundingMode.HALF_EVEN).multiply(retailLoanAttributes.getCorrectionCoefficient());
        BigDecimal calc1 = perIntRate.add(BigDecimal.valueOf(1L));
        BigDecimal calc2 = calc1.pow(retailLoanAttributes.getNumberOfPayments() );
        calc1 = calc2.subtract(BigDecimal.valueOf(1L));
        BigDecimal regularPayment  =  retailLoanAttributes.getLoanPrincipalAmount().multiply(calc2).multiply(perIntRate).divide(calc1,8,RoundingMode.HALF_EVEN);
        return regularPayment.round(new MathContext(6,RoundingMode.HALF_EVEN));
    }
}
