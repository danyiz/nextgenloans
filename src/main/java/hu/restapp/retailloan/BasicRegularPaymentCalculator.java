package hu.restapp.retailloan;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BasicRegularPaymentCalculator implements RegularPaymentCalculatorInterface {
    @Override
    public BigDecimal calculateRegularPayment(BigDecimal interestRate,BigDecimal loanAmount,int NumberOfPayments, BigDecimal CorrectionCoef) {
            //The formula P = iA / 1 - (1+i)^-N
        BigDecimal perIntRate = interestRate.divide(BigDecimal.valueOf(1200L),8, RoundingMode.HALF_EVEN).multiply(CorrectionCoef);
        BigDecimal calc1 = perIntRate.add(BigDecimal.valueOf(1L));
        BigDecimal calc2 = calc1.pow(NumberOfPayments );
        calc1 = calc2.subtract(BigDecimal.valueOf(1L));
        BigDecimal regularPayment  =  loanAmount.multiply(calc2).multiply(perIntRate).divide(calc1,8,RoundingMode.HALF_EVEN);
        return regularPayment.round(new MathContext(6,RoundingMode.HALF_EVEN));
    }
}
