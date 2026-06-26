package callimus.firstbankk;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class FirstBankUgandaForm extends JFrame {

    private final JTextField txtFirstName;
    private final JTextField txtLastName;
    private final JTextField txtNIN;
    private final JTextField txtEmail;
    private final JTextField txtConfirmEmail;
    private final JTextField txtPhone;
    private final JTextField txtDeposit;
    private JTextField txtSecondNIN;
    private final JPasswordField txtPIN;
    private final JPasswordField txtConfirmPIN;
    private final JComboBox<Integer> cbYear;
    private final JComboBox<Integer> cbDay;
    private final JComboBox<String> cbMonth;
    private final JComboBox<String> cbAccountType;
    private final JComboBox<String> cbBranch;
    private final JTextArea txtSummary;
    private JLabel lblSecondNIN;

    private static final Map<String, Integer> sequenceCounters = new HashMap<>();

    public FirstBankUgandaForm() {
        setTitle("First Bank Uganda - Desktop Account Provisioning Portal"); // [cite: 26]
        setSize(850, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int rowTracker = 0;
        addFormRow(mainPanel, gbc, rowTracker++, "First Name:", txtFirstName = new JTextField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "Last Name:", txtLastName = new JTextField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "National ID (NIN):", txtNIN = new JTextField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "Email Address:", txtEmail = new JTextField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "Confirm Email:", txtConfirmEmail = new JTextField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "Phone Number (+256...):", txtPhone = new JTextField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "PIN (4-6 digits):", txtPIN = new JPasswordField(20)); // [cite: 31]
        addFormRow(mainPanel, gbc, rowTracker++, "Confirm PIN:", txtConfirmPIN = new JPasswordField(20)); // [cite: 31]

        // Dynamic Date Handling setup layout
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        cbYear = new JComboBox<>();
        for (int y = LocalDate.now().getYear(); y >= LocalDate.now().getYear() - 90; y--) cbYear.addItem(y);
        cbMonth = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        cbDay = new JComboBox<>();
        dobPanel.add(new JLabel("Year:")); dobPanel.add(cbYear);
        dobPanel.add(new JLabel("Month:")); dobPanel.add(cbMonth);
        dobPanel.add(new JLabel("Day:")); dobPanel.add(cbDay); // [cite: 32]
        
        ActionListener dateConfigurator = e -> updateDaysInComboBox();
        cbYear.addActionListener(dateConfigurator);
        cbMonth.addActionListener(dateConfigurator);
        updateDaysInComboBox(); // Execute setup load

        gbc.gridx = 0; gbc.gridy = rowTracker; mainPanel.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 1; mainPanel.add(dobPanel, gbc);
        rowTracker++;

        cbAccountType = new JComboBox<>(new String[]{"Savings", "Current", "Fixed Deposit", "Student", "Joint"}); // [cite: 33]
        addFormRow(mainPanel, gbc, rowTracker++, "Account Type:", cbAccountType);

        lblSecondNIN = new JLabel("Second NIN (Joint Only):");
        txtSecondNIN = new JTextField(20);
        lblSecondNIN.setVisible(false); txtSecondNIN.setVisible(false);
        addFormRow(mainPanel, gbc, rowTracker++, lblSecondNIN, txtSecondNIN);

        cbAccountType.addActionListener(e -> {
            boolean isJoint = cbAccountType.getSelectedItem().toString().equals("Joint");
            lblSecondNIN.setVisible(isJoint);
            txtSecondNIN.setVisible(isJoint);
            revalidate(); repaint();
        });

        cbBranch = new JComboBox<>(new String[]{"Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"}); // [cite: 33]
        addFormRow(mainPanel, gbc, rowTracker++, "Branch:", cbBranch);
        addFormRow(mainPanel, gbc, rowTracker++, "Opening Deposit (UGX):", txtDeposit = new JTextField(20)); // [cite: 34]

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSubmit = new JButton("Submit Application");
        JButton btnReset = new JButton("Clear Form");
        buttonPanel.add(btnSubmit); buttonPanel.add(btnReset); // [cite: 35]

        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBorder(BorderFactory.createTitledBorder("Account Summary Is Below:")); // [cite: 35]
        txtSummary = new JTextArea(4, 50);
        txtSummary.setEditable(false);
        txtSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        southPanel.add(new JScrollPane(txtSummary), BorderLayout.CENTER);

        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        btnReset.addActionListener(e -> resetAllFields());
        btnSubmit.addActionListener(e -> processSubmission());
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.9; panel.add(comp, gbc);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel lbl, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1; panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.9; panel.add(comp, gbc);
    }

    private void updateDaysInComboBox() { // [cite: 32]
        if (cbYear.getSelectedItem() == null || cbMonth.getSelectedItem() == null) return;
        int selectedYear = (int) cbYear.getSelectedItem();
        int selectedMonthIndex = cbMonth.getSelectedIndex() + 1;
        int existingSelectedDay = (cbDay.getSelectedItem() != null) ? (int) cbDay.getSelectedItem() : 1;

        YearMonth yearMonthObject = YearMonth.of(selectedYear, selectedMonthIndex);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        cbDay.removeAllItems();
        for (int d = 1; d <= daysInMonth; d++) cbDay.addItem(d);
        if (existingSelectedDay <= daysInMonth) cbDay.setSelectedItem(existingSelectedDay);
        else cbDay.setSelectedIndex(daysInMonth - 1);
    }

    private void resetAllFields() { // [cite: 35]
        txtFirstName.setText(""); txtLastName.setText(""); txtNIN.setText("");
        txtEmail.setText(""); txtConfirmEmail.setText(""); txtPhone.setText("");
        txtPIN.setText(""); txtConfirmPIN.setText(""); txtDeposit.setText("");
        txtSecondNIN.setText(""); txtSummary.setText("");
        cbAccountType.setSelectedIndex(0); cbBranch.setSelectedIndex(0);
        cbYear.setSelectedIndex(0); cbMonth.setSelectedIndex(0);
    }

    private void processSubmission() {
        // Collect operational dataset values
        String fName = txtFirstName.getText().trim();
        String lName = txtLastName.getText().trim();
        String nin = txtNIN.getText().trim();
        String email = txtEmail.getText().trim();
        String confirmEmail = txtConfirmEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String pin = new String(txtPIN.getPassword()).trim();
        String confirmPin = new String(txtConfirmPIN.getPassword()).trim();
        String accType = cbAccountType.getSelectedItem().toString();
        String branch = cbBranch.getSelectedItem().toString();
        String depStr = txtDeposit.getText().trim();
        String secondNin = txtSecondNIN.getText().trim();

        double depositAmount = 0;
        try {
            depositAmount = Double.parseDouble(depStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "- Opening deposit must be typed as a number value.\n", "Parsing Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate birthDate = LocalDate.of((int) cbYear.getSelectedItem(), cbMonth.getSelectedIndex() + 1, (int) cbDay.getSelectedItem());

        // Delegate polymorphic object creation safely to our Factory Layer
        Account accountObj = AccountFactory.createAccount(accType, fName, lName, nin, email, phone, birthDate, branch, depositAmount, secondNin);

        // Run comprehensive validation tests against the decoupled model business rules matrix
        String evaluationErrors = FormValidator.validateFormInputs(fName, lName, nin, email, confirmEmail, phone, pin, confirmPin, depositAmount, accountObj);

        if (!evaluationErrors.isEmpty()) {
            JOptionPane.showMessageDialog(this, evaluationErrors, "Validation Incomplete", JOptionPane.ERROR_MESSAGE); // [cite: 44]
            return;
        }

        // Sequence ID Counter Generation Logic
        String branchCode = getBranchCode(branch);
        int currentYear = LocalDate.now().getYear();
        String counterKey = branchCode + "-" + currentYear;
        int currentSequence = sequenceCounters.getOrDefault(counterKey, 141) + 1; // Tracks per-year branch code limits [cite: 48]
        sequenceCounters.put(counterKey, currentSequence);
        
        String generatedAccountNumber = String.format("%s-%d-%06d", branchCode, currentYear, currentSequence); // [cite: 48]

        // Commit transaction data down to database manager persistence architecture layer
        try {
            DatabaseManager.saveAccountRecord(generatedAccountNumber, accountObj); // 

            String printableSummary = String.format("ACC: %s | %s %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                    generatedAccountNumber, accountObj.firstName, accountObj.lastName, accountObj.getAccountTypeName(),
                    accountObj.branch, accountObj.dob.toString(), accountObj.phoneNumber, accountObj.openingDeposit, accountObj.email);

            txtSummary.setText(printableSummary); // [cite: 45]
            JOptionPane.showMessageDialog(this, "Success: Account creation committed onto local database systems.", "Registration Complete", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database storage failure exception: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private String getBranchCode(String branchName) {
        switch (branchName) {
            case "Kampala": return "KLA"; // [cite: 48]
            case "Gulu": return "GUL"; // [cite: 48]
            case "Mbarara": return "MBR";
            case "Jinja": return "JNJ";
            case "Mbale": return "MBL";
            default: return "GEN";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FirstBankUgandaForm().setVisible(true));
    }
}