package org.example.carpooling.models.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

public class RegisterDto extends LoginDto {
    @NotEmpty(message = "First name can't be empty")
    @Size(min = 4, max = 32, message = "First Name must be between 4 and 32 symbols")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 4, max = 32, message = "Last Name must be between 4 and 32 symbols")
    private String lastName;
    @Unique
    @Email(message = "Email must be valid")
    @NotEmpty(message = "Email can't be empty")
    private String email;
    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;


    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
}
