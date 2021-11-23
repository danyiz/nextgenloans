package hu.restapp.retailloan;

import org.springframework.stereotype.Component;

@Component
public class RegularPaymentSplitterFactory implements RegularPaymentSplitterFactoryInterface {
    @Override
    public RegularPaymentSplitterInterface createRegularPaymentSplitter(RegularPaymentSplitterTypes calculatorType) {

        switch (calculatorType) {
            case PRINCIPAL_DELAYED:
                return new DelayedRegularPaymentSplitter();
            default:
                return new BasicRegularPaymentSplitter();
        }
    }
}
