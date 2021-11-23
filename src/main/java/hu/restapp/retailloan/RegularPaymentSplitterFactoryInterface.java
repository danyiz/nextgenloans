package hu.restapp.retailloan;


public interface RegularPaymentSplitterFactoryInterface {
    RegularPaymentSplitterInterface createRegularPaymentSplitter(RegularPaymentSplitterTypes calculatorType);
}

