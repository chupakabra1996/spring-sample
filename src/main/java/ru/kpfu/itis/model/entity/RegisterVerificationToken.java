package ru.kpfu.itis.model.entity;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "register_tokens")
public class RegisterVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime expired;

    @Column(nullable = false)
    private boolean verified;

    public RegisterVerificationToken() {}

    public RegisterVerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    //methods

    @PrePersist
    private void setUp() {
        expired = new LocalDateTime().plusDays(1);
        verified = false;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getExpired() {
        return expired;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    //equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterVerificationToken that = (RegisterVerificationToken) o;

        if (id != that.id) return false;
        if (verified != that.verified) return false;
        if (!token.equals(that.token)) return false;
        return user.equals(that.user);
        //        return expired.equals(that.expired);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + token.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + (verified ? 1 : 0);
        return result;
    }
}