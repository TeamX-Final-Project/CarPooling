package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserDto {
    private static final String NAME_CAN_T_BE_EMPTY_MESSAGE = "Name can't be empty";
    private static final String NUMBER_CAN_T_BE_EMPTY_MESSAGE = "Phone number can't be empty";
    private static final String NUMBER_SHOULD_BE_10_DIGITS_MESSAGE = "Phone number should be 10 digits";
    private static final String NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE = "Name should be between 2 and 20 symbols";
    private static final String PASSWORD_REQUIREMENTS_MESSAGE = "Password should be minimum 8 symbols, contain capital " +
            "letter and special symbol";

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String firstName;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String lastName;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String email;

    @NotNull(message = NUMBER_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 10, message = NUMBER_SHOULD_BE_10_DIGITS_MESSAGE)
    private String phoneNumber;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String username;

    @Size(min = 8, message = PASSWORD_REQUIREMENTS_MESSAGE)
    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String password;

    @Size(min = 8, message = PASSWORD_REQUIREMENTS_MESSAGE)
    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String passwordConfirm;

//    public UserDto() {
//    }
//
//    public UserDto(String firstName, String lastName, String email,
//                   String username, String phoneNumber,String password) {
//        setFirstName(firstName);
//        setLastName(lastName);
//        setEmail(email);
//        setUsername(username);
//        setPhoneNumber(phoneNumber);
//        setPassword(password);
//
//    }


    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public String getPassword() {
        return password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(email, userDto.email) && Objects.equals(username, userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username);
    }
}

