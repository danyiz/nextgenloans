package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PrincipalDelayedRegularPaymentCalculator implements RegularPaymentCalculatorInterface {
    @Autowired
    InterestCalculatorFactoryInterface interestCalculatorFactory;
    @Override
    public BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes) {
        retailLoanAttributes.setDaysInPeriod(30);
        InterestCalculatorInterface interestCalculatorInterface = interestCalculatorFactory.createInterestCalculator(retailLoanAttributes.getInterestCalculatorType());
        return interestCalculatorInterface.calculateInterestForPeriod(retailLoanAttributes).getInterestAmount().setScale(retailLoanAttributes.getCurrencyDecimals(), retailLoanAttributes.getCurrencyRoundingMode());

    }
}
