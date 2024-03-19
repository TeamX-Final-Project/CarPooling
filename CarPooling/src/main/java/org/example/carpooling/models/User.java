package org.example.carpooling.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.common.aliasing.qual.Unique;
import org.example.carpooling.models.enums.UserStatus;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    public static final String CAN_T_BE_EMPTY = "Can't be empty";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonIgnore
    private long userId;

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
    @Column(name = "profile_picture_url")
    private String profilePictureUrl =
            "https://static.vecteezy.com/system/resources/previews/020/765/399/non_2x/default-profile-account-unknown-icon-black-silhouette-free-vector.jpg";

    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private Set<Feedback> feedbackList;

    public User() {
    }

    public User(long userId, String firstName, String lastName, String email, String username, String phoneNumber,
                String password, UserStatus userStatus, boolean isAdmin) {

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id) {
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
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Set<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void setFeedbackList(Set<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
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
