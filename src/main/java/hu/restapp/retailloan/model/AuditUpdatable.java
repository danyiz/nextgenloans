package hu.restapp.retailloan.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
public class AuditUpdatable {

    @Column(name = "created_on",nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "created_by",nullable = false)
    private String createdBy;

    @Column(name = "updated_on",nullable = false)
    private LocalDateTime updatedOn;

    @Column(name = "updated_by",nullable = false)
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
        createdBy = "SYSTEM";
    }

    @PreUpdate
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
        updatedBy = "SYSTEM";
    }
}

