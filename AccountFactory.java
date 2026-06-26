package callimus.firstbankk;

import java.time.LocalDate;

public class AccountFactory {
    public static Account createAccount(String type, String fName, String lName, String nin, 
                                        String email, String phone, LocalDate dob, 
                                        String branch, double deposit, String secondNin) {
        switch (type) {
            case "Savings" -> {
                return new SavingsAccount(fName, lName, nin, email, phone, dob, branch, deposit);
            }
            case "Current" -> {
                return new CurrentAccount(fName, lName, nin, email, phone, dob, branch, deposit);
            }
            case "Fixed Deposit" -> {
                return new FixedDepositAccount(fName, lName, nin, email, phone, dob, branch, deposit);
            }
            case "Student" -> {
                return new StudentAccount(fName, lName, nin, email, phone, dob, branch, deposit);
            }
            case "Joint" -> {
                return new JointAccount(fName, lName, nin, email, phone, dob, branch, deposit, secondNin);
            }
            default -> throw new IllegalArgumentException("Unknown account type parameters provided: " + type);
        }
    }
}