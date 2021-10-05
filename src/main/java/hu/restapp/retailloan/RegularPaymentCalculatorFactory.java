package hu.restapp.retailloan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegularPaymentCalculatorFactory implements RegularPaymentCalculatorFactoryInterface{

    @Autowired
    PrincipalDelayedRegularPaymentCalculator principalDelayedRegularPaymentCalculator;

    @Autowired
    BasicRegularPaymentCalculator basicRegularPaymentCalculator;

    @Override
    public RegularPaymentCalculatorInterface createRegularPaymentCalculator(RegularPaymentCalculatorTypes calculatorType) {
        switch (calculatorType) {
            case PRINCIPAL_DELAYED:
                return principalDelayedRegularPaymentCalculator;
            default:
                return basicRegularPaymentCalculator;
        }
    }
}
