package hu.restapp.retailloan;

import hu.restapp.retailloan.model.RetailLoanAttributes;

import java.math.BigDecimal;

public interface RegularPaymentCalculatorInterface {
    BigDecimal calculateRegularPayment(RetailLoanAttributes retailLoanAttributes);
}
