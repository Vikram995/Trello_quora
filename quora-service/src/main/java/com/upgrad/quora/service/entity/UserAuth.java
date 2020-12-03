package com.upgrad.quora.service.entity;


import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_auth")
@NamedQueries(@NamedQuery(name = "getUserAuthByToken", query = "select u from UserAuth u where u.token = :token"))
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "uuid")
    private String uuid;


    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "access_token")
    private String token;


    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;


    @Column(name = "login_at")
    private ZonedDateTime loginAt;


    @Column(name = "logout_at")
    private ZonedDateTime logoutAt;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }
}
