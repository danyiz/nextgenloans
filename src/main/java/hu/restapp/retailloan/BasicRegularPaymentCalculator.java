package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BasicRegularPaymentCalculator implements RegularPaymentCalculatorInterface {
    @Override
    public BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes) {
            //The formula P = iA / 1 - (1+i)^-N
        BigDecimal perIntRate = retailLoanAttributes.getLoanInterestRate().divide(BigDecimal.valueOf(1200L),8, RoundingMode.DOWN).multiply(retailLoanAttributes.getCorrectionCoefficient());
        BigDecimal calc1 = perIntRate.add(BigDecimal.valueOf(1L));
        int numberOfPayments = retailLoanAttributes.getNumberOfPayments() - (retailLoanAttributes.getNextPaymentNumber() -1);
        double intA = Math.pow(calc1.doubleValue(),((-1)*numberOfPayments));
        BigDecimal calc2 =  new BigDecimal("1").subtract(BigDecimal.valueOf(intA).setScale(8,RoundingMode.DOWN));
        BigDecimal regularPayment  =  retailLoanAttributes.getLoanPrincipalAmount().multiply(perIntRate).divide(calc2,8,RoundingMode.DOWN);
        return regularPayment.setScale(retailLoanAttributes.getCurrencyDecimals(), retailLoanAttributes.getCurrencyRoundingMode());
    }
}
