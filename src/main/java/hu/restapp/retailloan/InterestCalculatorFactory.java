package hu.restapp.retailloan;

import org.springframework.stereotype.Service;

@Service
public class InterestCalculatorFactory implements InterestCalculatorFactoryInterface{
    @Override
    public InterestCalculatorInterface createInterestCalculator(InterestCalculatorTypes calculatorType) {
        switch (calculatorType) {
            case NEW_ANNUITY_FORMULA:
                return new NewAnnuityInterestCalculator();
            case BASIC360DIV12:
            default:
                return new BasicLoanInterestCalculator();
        }
    }
}
