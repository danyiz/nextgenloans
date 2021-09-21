package hu.restapp.retailloan.model;

import hu.restapp.retailloan.model.AuditHistory;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
public abstract class RetailLoanBaseHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Embedded
    private AuditHistory audit = new AuditHistory();
    @Column(nullable = false)
    private Long dailySequence;
    @Column(nullable = false)
    private LocalDate valueDate;
    @Column(nullable = false)
    private String accountNumber;
}
