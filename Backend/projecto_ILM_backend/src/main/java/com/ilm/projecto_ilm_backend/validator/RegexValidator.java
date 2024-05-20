package com.ilm.projecto_ilm_backend.validator;

/**
 * Validator class for regex-based validations.
 */
public class RegexValidator {

    /**
     * Validates the provided email using a regular expression.
     *
     * @param email the email to be validated
     * @return {@code true} if the email matches the regex pattern, {@code false} otherwise
     */
    public static boolean validateEmail(String email) {
        // Regex to validate email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Validates the provided password using a regular expression. The password must:
     * - Contain at least one uppercase letter
     * - Contain at least one digit
     * - Be between 6 and 20 characters long
     * - Contain at least one special character
     * - Not contain any whitespace
     *
     * @param password the password to be validated
     * @return {@code true} if the password matches the regex pattern, {@code false} otherwise
     */
    public static boolean validatePassword(String password) {
        // Regex to validate password
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}$";
        return password.matches(passwordRegex);
    }
}
