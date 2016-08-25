package ru.kpfu.itis.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "persistent_login")
public class PersistentLogin {

    @Id
    private String series;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_used;


    //methods

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLast_used() {
        return last_used;
    }

    public void setLast_used(Date last_used) {
        this.last_used = last_used;
    }

    //equals and hashcode


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersistentLogin that = (PersistentLogin) o;

        if (!series.equals(that.series)) return false;
        if (!username.equals(that.username)) return false;
        if (!token.equals(that.token)) return false;
        return last_used.equals(that.last_used);

    }

    @Override
    public int hashCode() {
        int result = series.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + token.hashCode();
        result = 31 * result + last_used.hashCode();
        return result;
    }
}