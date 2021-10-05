package hu.restapp.retailloan.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@ToString
@AllArgsConstructor
public class RetailLoanScheduleDTO {
    @Getter@Setter
    private LocalDate valueDate;
    @Getter@Setter
    private String currencyCode;
    @Getter@Setter
    private String accountNumber;
    @Getter@Setter
    private int numberOfPayments;
    @Getter@Setter
    private BigDecimal loanPrincipalAmount;
    @Getter@Setter
    private BigDecimal interestPart;
    @Getter@Setter
    private BigDecimal principalPart;
    @Getter@Setter
    private BigDecimal regularPayment;
    @Getter@Setter
    private BigDecimal loanInterestRate;
    @Getter@Setter
    private BigDecimal subsidizedInterestRate;
    @Getter@Setter
    private BigDecimal interestBonusRate;
    @Setter
    private BigDecimal effectiveInterestRate;
    @Getter@Setter
    private Integer nextPaymentNumber;

    public BigDecimal getEffectiveInterestRate(BigDecimal standardInterestRate) {
        return standardInterestRate.subtract(interestBonusRate).subtract(subsidizedInterestRate) ;
    }
}