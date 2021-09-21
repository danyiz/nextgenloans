package hu.restapp.retailloan;

public interface RegularPaymentCalculatorFactoryInterface {
    RegularPaymentCalculatorInterface createRegularPaymentCalculator(RegularPaymentCalculatorTypes calculatorType);
}
