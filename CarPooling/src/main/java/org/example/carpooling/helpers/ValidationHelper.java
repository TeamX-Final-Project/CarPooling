package org.example.carpooling.helpers;

import org.example.carpooling.exceptions.ValidatePasswordException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–\\[{}\\]:;',?\\/*~$\\^+=<>]).{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    private static final String INVALID_PASSWORD =
            "Password must be at least 8 symbols and must contain lowercase, uppercase letters, digit and special symbol";

    public static void validatePassword(final String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new ValidatePasswordException(INVALID_PASSWORD);
        }
    }
}
