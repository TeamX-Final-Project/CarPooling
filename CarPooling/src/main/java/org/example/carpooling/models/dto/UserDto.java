package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UserDto {
    public static final String NAME_CAN_T_BE_EMPTY_MESSAGE = "Name can't be empty";
    private static final String NUMBER_CAN_T_BE_EMPTY_MESSAGE = "Phone number can't be empty";
    private static final String NUMBER_SHOULD_BE_10_DIGITS_MESSAGE = "Phone number should be 10 digits";
    public static final String NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE = "Name should be between 2 and 20 symbols";
    private static final String PASSWORD_REQUIREMENTS_MESSAGE = "Password should be minimum 8 symbols, contain capital " +
            "letter and special symbol";
    public static final String EMAIL_CAN_T_BE_EMPTY_MESSAGE = "E-mail can't be empty";
    public static final String PASSWORD_CAN_T_BE_EMPTY_MESSAGE = "Password can't be empty";
    public static final String EMAIL_IS_REQUIRED_MESSAGE = "E-mail is required";
    public static final String NAME_IS_REQUIRED_MESSAGE = "Username is required";

    private long id;

    @NotNull(message = NAME_IS_REQUIRED_MESSAGE)
    @NotEmpty(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    private String username;

    @NotNull(message = EMAIL_IS_REQUIRED_MESSAGE)
    @NotEmpty(message = EMAIL_CAN_T_BE_EMPTY_MESSAGE)
    private String email;

    @NotNull(message = NUMBER_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 10, message = NUMBER_SHOULD_BE_10_DIGITS_MESSAGE)
    private String phoneNumber;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String firstName;

    @NotNull(message = NAME_CAN_T_BE_EMPTY_MESSAGE)
    @Size(min = 2, max = 20, message = NAME_SHOULD_BE_BETWEEN_2_AND_20_SYMBOLS_MESSAGE)
    private String lastName;


    @Size(min = 8, message = PASSWORD_REQUIREMENTS_MESSAGE)
    @NotNull(message = PASSWORD_CAN_T_BE_EMPTY_MESSAGE)
    private String password;

    @Size(min = 8, message = PASSWORD_REQUIREMENTS_MESSAGE)
    @NotNull(message = PASSWORD_CAN_T_BE_EMPTY_MESSAGE)
    private String passwordConfirm;


private String profilePhoto;


    public UserDto() {
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

