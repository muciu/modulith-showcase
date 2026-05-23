package com.muciociosan.theproject.users.domain;


import com.muciociosan.theproject.shared.exceptions.InvalidStateException;
import com.muciociosan.theproject.shared.exceptions.ValidationException;
import com.muciociosan.theproject.users.usecases.EmailValue;
import com.muciociosan.theproject.users.view.UserEmailView;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_emails")
@EntityListeners(AuditingEntityListener.class)
public class UserEmailEntity {
    @Id
    private UUID id;

    @Column(name = "email", updatable = false, nullable = false)
    private String email;

    @Column(name = "is_current", nullable = false)
    private boolean current = false;

    // Note: purposely using string in case new value is added on the database side during rolling upgrade
    @Column(name = "validation_status", nullable = false)
    private String verificationStatus = VerificationStatus.NEW.name();

    @Column(name = "verification_date")
    private Instant verificationDate;

    // -- auditing goes below
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    protected UserEmailEntity(){
        // for hibernate with love
    }

    private UserEmailEntity(final UUID id, final EmailValue email) {
        this.id = id;
        this.email = email.value();
    }

    public static UserEmailEntity newFrom(final EmailValue email) {
        return new UserEmailEntity(UUID.randomUUID(), email);
    }

    public void markVerified() {
        if (verificationStatus() != VerificationStatus.VERIFICATION_STARTED) {
            throw new ValidationException("verificationStatus", "Unable to change status from %s".formatted(verificationStatus()));
        }
        this.verificationDate = Instant.now();
        this.verificationStatus = VerificationStatus.VERIFIED.name();
        this.current = true;
    }

    public VerificationStatus verificationStatus() {
        return VerificationStatus.valueOf(verificationStatus);
    }

    @Nullable Instant verificationDate() {
        return verificationDate;
    }

    public EmailValue email() {
        return EmailValue.from(email);
    }

    public boolean current() {
        return current;
    }

    void unmarkCurrent() {
        if (!this.current) {
            throw new InvalidStateException("Email is not current!");
        }
        this.current = false;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void verificationStarted() {
        this.verificationStatus = VerificationStatus.VERIFICATION_STARTED.name();
        // TODO store verification_stared date as well
    }

    public enum VerificationStatus {
        NEW,
        VERIFICATION_STARTED,
        VERIFIED,
        VERIFICATION_ETA_EXPIRED;
    }

    public UserEmailView view() {
        return new UserEmailView(
                EmailValue.from(this.email),
                verificationStatus() == VerificationStatus.VERIFIED
        );
    }
}
