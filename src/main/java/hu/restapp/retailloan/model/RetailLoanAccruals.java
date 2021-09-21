package hu.restapp.retailloan.model;

import hu.restapp.retailloan.model.RetailLoanBaseHistory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "RetailLoanAccruals")
public class RetailLoanAccruals extends RetailLoanBaseHistory {

    @Column(nullable = false)
    private BigDecimal dailyInterest;
    @Column(nullable = false)
    private BigDecimal dailyInterestWash;
    @Column(nullable = false)
    private BigDecimal interestAccruedPeriodToDate;
    @Column(nullable = false)
    private LocalDate interestLastBringUpDate;
}
