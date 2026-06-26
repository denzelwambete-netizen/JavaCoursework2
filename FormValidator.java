package callimus.firstbankk;

public class FormValidator {

    public static String validateFormInputs(String fName, String lName, String nin, String email, 
                                            String confirmEmail, String phone, String pin, 
                                            String confirmPin, double depositAmount, Account accountObj) {
        StringBuilder errors = new StringBuilder();

        // Standard Regular Expression Validations
        if (!fName.matches("^[a-zA-Z]{2,30}$")) errors.append("- First Name must contain letters only (2-30 characters).\n"); // [cite: 41]
        if (!lName.matches("^[a-zA-Z]{2,30}$")) errors.append("- Last Name must contain letters only (2-30 characters).\n"); // [cite: 41]
        if (!nin.matches("^[A-Z0-9]{14}$")) errors.append("- National ID (NIN) must be exactly 14 UPPERCASE alphanumeric characters.\n"); // [cite: 42]
        
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) errors.append("- Provide a valid Email address format.\n"); // [cite: 42]
        if (!email.equalsIgnoreCase(confirmEmail)) errors.append("- Email confirmation does not match.\n"); // [cite: 42]
        
        if (!phone.matches("^\\+256\\d{9}$")) errors.append("- Phone number must use Ugandan format: +256XXXXXXXXX (9 digits following prefix).\n"); // [cite: 42]
        
        if (!pin.matches("^\\d{4,6}$")) errors.append("- PIN must be numeric only and between 4 to 6 digits long.\n"); // [cite: 42]
        if (!pin.equals(confirmPin)) errors.append("- PIN confirmation entries do not match.\n"); // [cite: 42]
        if (pin.matches("^(\\d)\\1+$")) errors.append("- PIN security exception: Repeated single digit combinations are banned.\n"); // [cite: 42]

        if (accountObj != null) {
            // General Target Bound Checks
            int calculatedAge = accountObj.getAge(); // [cite: 42]
            if (calculatedAge < 18 || calculatedAge > 75) { // [cite: 42]
                errors.append("- Account applicants must be between 18 and 75 years old (Current age: ").append(calculatedAge).append(").\n");
            }

            // Polymorphic Minimum Deposit Bounds Checker Execution
            if (depositAmount < accountObj.getMinimumDeposit()) { // [cite: 42, 52]
                errors.append("- Selected account configuration [").append(accountObj.getAccountTypeName())
                      .append("] requires a minimum layout of UGX ").append(String.format("%,.0f", accountObj.getMinimumDeposit())).append(".\n");
            }

            // Append specific contextual inner exception validations
            errors.append(accountObj.validateSpecificRules());
        }

        return errors.toString();
    }
}