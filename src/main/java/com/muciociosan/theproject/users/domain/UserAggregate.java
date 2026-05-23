package com.muciociosan.theproject.users.domain;

import com.muciociosan.theproject.shared.exceptions.InvalidStateException;
import com.muciociosan.theproject.shared.ids.UserId;
import com.muciociosan.theproject.users.domain.view.UserView;
import com.muciociosan.theproject.users.usecases.EmailValue;
import jakarta.persistence.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

import static com.muciociosan.theproject.users.domain.UserEmailEntity.VerificationStatus.VERIFICATION_STARTED;
import static java.util.Comparator.comparing;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@AggregateRoot
public class UserAggregate {

    @Id
    @Identity
    @Column(name = "id")
    private UUID id;

    @Column(name = "username", updatable = false, nullable = false)
    private UsernameValue username;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private Set<UserEmailEntity> emails = new HashSet<>();

    @Transient
    private List<UserAggregateEvent> pendingEvents = new ArrayList<>();

    protected UserAggregate() {
        // for hibernate with love
    }

    protected UserAggregate(final UserId userId, final UsernameValue username, final String email) {
        this.id = userId.uuid();
        this.username = username;
        this.emails.add(UserEmailEntity.newFrom(EmailValue.from(email)));
        this.pendingEvents.add(new UserCreated(UserId.from(this.id)));
    }

    public static UserAggregate newFrom(final UsernameValue username, final String email) {
        return new UserAggregate(UserId.random(), username, email);
    }

    public UserId id() {
        return UserId.fromNullable(id);
    }

    public UsernameValue username() {
        return username;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * Exposes publicly (outside module) available view of the aggregate
     */
    public UserView view() {
        final var emailToReturn = currentEmail().view();
        return new UserView(username().value(), emailToReturn, createdAt(), updatedAt());
    }

    public void updateEmail(final EmailValue newEmail) {
        if (currentEmail().email().equals(newEmail)) {
            return;
        }
        this.emails.add(UserEmailEntity.newFrom(newEmail));
        this.pendingEvents.add(new UserEmailAdded(id(), newEmail.value()));
    }

    /**
     * Current email is the one that is marked as 'current' (means verified) or most recently added and NOT verified
     * There is DB constraint that prevent multiple current emails per one user_id (uk_user_emails_current_per_user)
     */
    private UserEmailEntity currentEmail() {
        final var currentEmail = this.emails.stream().filter(UserEmailEntity::current).findFirst();
        return currentEmail.or(() -> this.emails.stream().min(comparing(UserEmailEntity::createdAt)))
                .orElseThrow();
    }

    public void emailVerificationStarted(final EmailValue emailValue) {
        this.emails.stream()
                .filter(email -> email.email().equals(emailValue))
                .peek(UserEmailEntity::verificationStarted).findFirst()
                .orElseThrow(() -> new InvalidStateException("No email to verify"));
    }

    public void markEmailVerified(final EmailValue emailValue) {
        final var toMark = this.emails.stream()
                .filter(email -> email.email().equals(emailValue))
                .filter(email -> email.verificationStatus() == VERIFICATION_STARTED)
                .findFirst().orElseThrow(() -> new InvalidStateException("No email to verify"));
        toMark.markVerified();// this should fail because we have constraint
    }

    // ------ INTERNAL methods goes below

    List<UserAggregateEvent> resetPendingEvents() {
        final var events = List.copyOf(pendingEvents);
        this.pendingEvents = new ArrayList<>();
        return events;
    }
}
