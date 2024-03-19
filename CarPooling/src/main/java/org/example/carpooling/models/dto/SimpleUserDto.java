package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.carpooling.models.enums.UserStatus;

import static org.example.carpooling.models.dto.UserDto.*;

public class SimpleUserDto {

    private long id;

    @NotNull(message = NAME_IS_REQUIRED_MESSAGE)
    @NotEmpty(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String username;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String firstName;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String lastName;

    @NotNull(message = EMAIL_IS_REQUIRED_MESSAGE)
    @NotEmpty(message = EMAIL_CAN_T_BE_EMPTY_MESSAGE)
    private String email;

    @NotNull(message = NUMBER_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 10, message = NUMBER_SHOULD_BE_10_DIGITS_MESSAGE)
    private String phoneNumber;

    private UserStatus status;
    private boolean isAdmin;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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
}
