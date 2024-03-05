package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.carpooling.models.enums.UserStatus;

import static org.example.carpooling.models.dto.UserDto.*;

public class SimpleUserDto {

    private int id;

    @NotNull(message = NAME_IS_REQUIRED_MESSAGE)
    @NotEmpty(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String username;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String firstName;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String lastName;


    private UserStatus status;
    private boolean isAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
