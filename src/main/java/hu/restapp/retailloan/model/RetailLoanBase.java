package hu.restapp.retailloan.model;

import hu.restapp.retailloan.model.AuditUpdatable;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class RetailLoanBase implements Serializable {

    @Embedded
    private AuditUpdatable audit = new AuditUpdatable();
    @Id
    @Column(nullable = false,unique=true)
    private String accountNumber;
}
