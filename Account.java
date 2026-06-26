package callimus.firstbankk;

import java.time.LocalDate;
import java.time.Period;

public abstract class Account {
    public final String firstName;
    public final String lastName;
    public final String nin;
    public final String email;
    public final String phoneNumber;
    public final LocalDate dob;
    public final String branch;
    public final double openingDeposit;

    public Account(String firstName, String lastName, String nin, String email, 
                   String phoneNumber, LocalDate dob, String branch, double openingDeposit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
    }

    public abstract double getMinimumDeposit();
    public abstract String getAccountTypeName();

    public int getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public String validateSpecificRules() {
        return ""; 
    }
}

class SavingsAccount extends Account {
    public SavingsAccount(String f, String l, String n, String e, String p, LocalDate d, String b, double dep) {
        super(f, l, n, e, p, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 50000; } // [cite: 38]
    @Override public String getAccountTypeName() { return "Savings"; } // [cite: 33]
}

class CurrentAccount extends Account {
    public CurrentAccount(String f, String l, String n, String e, String p, LocalDate d, String b, double dep) {
        super(f, l, n, e, p, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 200000; } // [cite: 38]
    @Override public String getAccountTypeName() { return "Current"; } // [cite: 33]
}

class FixedDepositAccount extends Account {
    public FixedDepositAccount(String f, String l, String n, String e, String p, LocalDate d, String b, double dep) {
        super(f, l, n, e, p, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 1000000; } // [cite: 38]
    @Override public String getAccountTypeName() { return "Fixed Deposit"; } // [cite: 33]
}

class StudentAccount extends Account {
    public StudentAccount(String f, String l, String n, String e, String p, LocalDate d, String b, double dep) {
        super(f, l, n, e, p, d, b, dep);
    }
    @Override public double getMinimumDeposit() { return 10000; } // [cite: 38]
    @Override public String getAccountTypeName() { return "Student"; } // [cite: 33]
    
    @Override
    public String validateSpecificRules() {
        int age = getAge();
        if (age < 18 || age > 25) { // [cite: 38, 42]
            return "- Student account applicants must be between 18 and 25 years old (Current age: " + age + ").\n";
        }
        return "";
    }
}

class JointAccount extends Account {
    private final String secondNin;

    public JointAccount(String f, String l, String n, String e, String p, LocalDate d, String b, double dep, String secondNin) {
        super(f, l, n, e, p, d, b, dep);
        this.secondNin = secondNin;
    }
    @Override public double getMinimumDeposit() { return 100000; } // [cite: 38]
    @Override public String getAccountTypeName() { return "Joint"; } // [cite: 33]

    @Override
    public String validateSpecificRules() {
        if (secondNin == null || !secondNin.trim().matches("^[A-Z0-9]{14}$")) { // [cite: 38, 42]
            return "- Joint accounts require a valid Second NIN matching exactly 14 UPPERCASE alphanumeric characters.\n";
        }
        return "";
    }
}