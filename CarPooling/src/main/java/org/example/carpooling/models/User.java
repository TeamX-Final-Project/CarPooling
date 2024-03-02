package org.example.carpooling.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.common.aliasing.qual.Unique;
import org.example.carpooling.models.enums.UserStatus;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    public static final String CAN_T_BE_EMPTY = "Can't be empty";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonIgnore
    private int userId;

    @NotNull(message = CAN_T_BE_EMPTY)
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = CAN_T_BE_EMPTY)
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = CAN_T_BE_EMPTY)
    @Email
    @Column(name = "email")
    private String email;

    @NotNull(message = CAN_T_BE_EMPTY)
    @Column(name = "username")
    private String username;

    @NotNull(message = CAN_T_BE_EMPTY)
    @Column(name = "phone_number")
    private String phoneNumber;
    @JsonIgnore
    @NotNull(message = CAN_T_BE_EMPTY)
    @Column(name = "password")
    private String password;
    @JsonIgnore
    @Column(name = "is_admin")
    private boolean isAdmin;
    @JsonIgnore
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_status")
    private UserStatus userStatus;

    public User() {
    }

    public User(int userId, String firstName, String lastName, String email, String username, String phoneNumber,
                String password, UserStatus userStatus, boolean isAdmin) {
//        setId(id);
//        setFirstName(firstName);
//        setLastName(lastName);
//        setEmail(email);
//        setUsername(username);
//        setPassword(password);
//        setBlocked(isBlocked);
//        setBlocked(isDeleted);
//        setAdmin(isAdmin);

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userStatus = userStatus;
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
