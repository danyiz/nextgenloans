package hu.restapp.retailloan.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Embeddable
public class AuditHistory {

    @Column(name = "created_on",nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
        createdBy = "SYSTEM";
    }
}

