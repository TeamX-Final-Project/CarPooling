package org.example.carpooling.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "user_security_code")
public class UserSecurityCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "security_code")
    private long securityCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(long securityCode) {
        this.securityCode = securityCode;
    }
}
