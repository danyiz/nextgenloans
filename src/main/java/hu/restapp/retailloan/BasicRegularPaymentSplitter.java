package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import hu.restapp.retailloan.model.RetailLoanSchedule;

public class BasicRegularPaymentSplitter implements RegularPaymentSplitterInterface {
    @Override
    public RetailLoanAttributes splitRegularPayment(RetailLoanAttributes retailLoanAttributes) {
        retailLoanAttributes.setPrincipalPart(retailLoanAttributes.getRegularPayment().subtract(retailLoanAttributes.getInterestPart()));
        return retailLoanAttributes;
    }
}
