package org.softuni.bookshopbg.model.security;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import org.softuni.bookshopbg.model.entities.BaseEntity;
import org.softuni.bookshopbg.model.entities.UserEntity;

import java.util.Calendar;
import java.util.Date;



@Entity
public class PasswordResetToken extends BaseEntity {

    private static final int EXPIRATION = 60 * 24;


    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable=false, name="user_id")
    private UserEntity user;

    private Date expiryDate;

    public PasswordResetToken(){}

    public PasswordResetToken(final String token, final UserEntity user) {
        super ();

        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate (final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public static int getExpiration() {
        return EXPIRATION;
    }

    @Override
    public String toString() {
        return "PasswordResetToken [token=" + token + ", user=" + user + ", expiryDate=" + expiryDate
                + "]";
    }


}
