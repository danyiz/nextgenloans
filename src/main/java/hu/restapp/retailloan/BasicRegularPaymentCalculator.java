package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class BasicRegularPaymentCalculator implements RegularPaymentCalculatorInterface {
    @Override
    public BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes) {
            //The formula P = iA / 1 - (1+i)^-N
        BigDecimal perIntRate = retailLoanAttributes.getLoanInterestRate().divide(BigDecimal.valueOf(1200L),8, RoundingMode.DOWN).multiply(retailLoanAttributes.getCorrectionCoefficient());
        BigDecimal calc1 = perIntRate.add(BigDecimal.valueOf(1L));
        BigDecimal calc2 = calc1.pow(retailLoanAttributes.getNumberOfPayments() - retailLoanAttributes.getNextPaymentNumber() +1 ).setScale(8,RoundingMode.DOWN);
        calc1 = calc2.subtract(BigDecimal.valueOf(1L));
        BigDecimal regularPayment  =  retailLoanAttributes.getLoanPrincipalAmount().multiply(calc2).multiply(perIntRate).divide(calc1,8,RoundingMode.DOWN);
        return regularPayment.setScale(retailLoanAttributes.getCurrencyDecimals(), retailLoanAttributes.getCurrencyRoundingMode());
    }
}
