package hu.restapp.retailloan.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@AllArgsConstructor
public class RetailLoanScheduleDTO {
    @Getter@Setter
    LocalDate valueDate;
    @Getter@Setter
    String accountNumber;
    @Getter@Setter
    public int numberOfPayments;
    @Getter@Setter
    public BigDecimal outstandingPrincipal;
    @Getter@Setter
    public BigDecimal interestPart;
    @Getter@Setter
    public BigDecimal principalPart;
    @Getter@Setter
    public BigDecimal regularPayment;
    @Getter@Setter
    public BigDecimal standardInterestRate;
    @Getter@Setter
    public BigDecimal subsidizedInterestRate;
    @Getter@Setter
    public BigDecimal interestBonusRate;
    @Setter
    public BigDecimal effectiveInterestRate;

    public BigDecimal getEffectiveInterestRate(BigDecimal standardInterestRate) {
        return standardInterestRate.subtract(interestBonusRate).subtract(subsidizedInterestRate) ;
    }
}