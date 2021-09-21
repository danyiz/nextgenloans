package hu.restapp.retailloan;

import org.springframework.stereotype.Service;

@Service
public class InterestCalculatorFactory implements InterestCalculatorFactoryInterface{
    @Override
    public InterestCalculatorInterface createInterestCalculator(InterestCalculatorTypes calculatorType) {
        switch (calculatorType) {
            case BASIC:
            default:
                return new BasicLoanInterestCalculator();
        }
    }
}
