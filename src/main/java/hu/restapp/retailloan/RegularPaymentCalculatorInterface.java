package hu.restapp.retailloan;

import java.math.BigDecimal;

public interface RegularPaymentCalculatorInterface {
    BigDecimal calculateRegularPayment(BigDecimal interestRate,BigDecimal loanAmount,int NumberOfPayments, BigDecimal CorrectionCoef);
}
