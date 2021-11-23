package hu.restapp.retailloan.model;

import hu.restapp.retailloan.model.RetailLoanBaseHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "RetailLoanBalances")
public class RetailLoanBalances extends RetailLoanBaseHistory {

    @Column(nullable = false)
    private BigDecimal overPaymentAmount;
    @Column(nullable = false)
    private BigDecimal outstandingPrincipalAmount;
    @Column(nullable = false)
    private BigDecimal principalDueAmount;
    @Column(nullable = false)
    private BigDecimal interestDueAmount;
    @Column(nullable = false)
    private BigDecimal managementFeeDueAmount;
    @Column(nullable = false)
    private BigDecimal feeDueAmount;
    @Column(nullable = false)
    private BigDecimal finalDemandFeeDueAmount;

}
