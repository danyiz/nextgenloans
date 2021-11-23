package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;
import hu.restapp.retailloan.model.RetailLoanSchedule;

public interface RegularPaymentSplitterInterface {
    RetailLoanAttributes splitRegularPayment(RetailLoanAttributes retailLoanAttributes);
}
