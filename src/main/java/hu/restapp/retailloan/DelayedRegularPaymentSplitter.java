package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import hu.restapp.retailloan.model.RetailLoanSchedule;

import java.math.BigDecimal;

public class DelayedRegularPaymentSplitter implements RegularPaymentSplitterInterface {
    @Override
    public RetailLoanAttributes splitRegularPayment(RetailLoanAttributes retailLoanAttributes) {
        retailLoanAttributes.setPrincipalPart(new BigDecimal("0"));
        return retailLoanAttributes;
    }
}
