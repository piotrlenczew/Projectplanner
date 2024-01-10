package pw.pap.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    @Column(name = "google_id")
    private String googleId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "account_creation_date", nullable = false, updatable = false)
    private LocalDateTime accountCreationDate;

    public User() { }

    public User(String name, LocalDateTime accountCreationDate) {
        this.name = name;
        this.accountCreationDate = accountCreationDate;
    }

    public User(String name, String passwordHash, String salt, LocalDateTime accountCreationDate) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.accountCreationDate = accountCreationDate;
    }

    public User(String name, String passwordHash, String salt, String email, String googleId, LocalDateTime accountCreationDate) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.email = email;
        this.googleId = googleId;
        this.accountCreationDate = accountCreationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(LocalDateTime accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }
}
