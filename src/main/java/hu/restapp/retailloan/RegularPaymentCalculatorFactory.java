package hu.restapp.retailloan;

import org.springframework.stereotype.Service;

@Service
public class RegularPaymentCalculatorFactory implements RegularPaymentCalculatorFactoryInterface{

    @Override
    public RegularPaymentCalculatorInterface createRegularPaymentCalculator(RegularPaymentCalculatorTypes calculatorType) {
        switch (calculatorType) {
            case BASIC:
            default:
                return new BasicRegularPaymentCalculator();
        }
    }
}
