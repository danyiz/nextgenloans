package hu.restapp.retailloan;

public interface InterestCalculatorFactoryInterface {
    InterestCalculatorInterface createInterestCalculator(InterestCalculatorTypes calculatorType);
}
