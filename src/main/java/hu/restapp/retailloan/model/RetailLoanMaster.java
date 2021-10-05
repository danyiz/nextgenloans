package hu.restapp.retailloan.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "RetailLoanMaster")
public class RetailLoanMaster extends RetailLoanBase {
    @Column(length = 24,nullable = false)
    String publicAccountNumber;
    @Column(nullable = false)
    Integer currency;
    @Column(nullable = false)
    BigDecimal originalLoanAmount;
    @Column(nullable = false)
    BigDecimal regularPaymentAmount;
    @Column(length = 3,nullable = false)
    Integer originalNumberOfPayments;
    @Column(length = 3,nullable = false)
    Integer numberOfPayments;
    @Column(nullable = false)
    BigDecimal monthlyManagementFeeAmount;
    @Column(nullable = false)
    LocalDate nextStatementDate;
    @Column(nullable = false)
    LocalDate nextInterestSettlementDate;
    @Column(nullable = false)
    LocalDate regularPaymentCalculationDate;
    @Column(length = 10,nullable = false)
    String interestPaymentFrequency;
    @Column(length = 10,nullable = false)
    String principalPaymentFrequency;
    @Column(length = 10,nullable = false)
    String paymentMethod;
    @Column(nullable = false)
    BigDecimal interestEstimatedForThePeriod;
    @Column(nullable = false)
    BigDecimal principalEstimatedForThePeriod;

}
