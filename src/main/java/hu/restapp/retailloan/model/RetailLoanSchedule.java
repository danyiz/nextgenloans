package hu.restapp.retailloan.model;

import hu.restapp.retailloan.model.AuditHistory;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name="RetailLoanSchedule")
public class RetailLoanSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Embedded
    private AuditHistory audit = new AuditHistory();
    @Column//(nullable = false)
    private Long dailySequence;
    @Column//(nullable = false)
    private LocalDate valueDate;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String currencyCode;
    @Column(nullable = false)
    private Integer numberOfPayments;
    @Column//(nullable = false)
    private Integer termsToDelayPrincipalPayment;
    @Column//(nullable = false)
    private Integer nextPaymentNumber;
    @Column//(nullable = false)
    private BigDecimal loanPrincipalAmount;
    @Column//(nullable = false)
    private BigDecimal interestPart;
    @Column//(nullable = false)
    private BigDecimal principalPart;
    @Column//(nullable = false)
    private BigDecimal regularPayment;
    @Column//(nullable = false)
    private BigDecimal loanInterestRate;
    @Column
    private BigDecimal subsidizedInterestRate;
    @Column
    private BigDecimal interestBonusRate;
    @Column
    private BigDecimal effectiveInterestRate;
}