import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HospitalGUI extends JFrame {
    private JTextField nameField, ageField, ailmentField;
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public HospitalGUI() {
        // Build the database automatically when the app starts
        DatabaseHelper.initializeDatabase();

        setTitle("Hospital Patient Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL: Input Form ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Admit New Patient"));

        inputPanel.add(new JLabel("Patient Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Ailment/Symptoms:"));
        ailmentField = new JTextField();
        inputPanel.add(ailmentField);

        JButton admitButton = new JButton("Admit Patient");
        admitButton.setBackground(new Color(46, 204, 113));
        admitButton.setForeground(Color.WHITE);
        admitButton.setFocusPainted(false);
        inputPanel.add(new JLabel(""));
        inputPanel.add(admitButton);

        add(inputPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: Data Table ---
        String[] columns = {"Patient ID", "Name", "Age", "Ailment", "Arrival Time"};
        tableModel = new DefaultTableModel(columns, 0);
        patientTable = new JTable(tableModel);
        patientTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Waiting Queue"));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL: Controls ---
        JPanel controlPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        controlPanel.add(refreshButton);
        add(controlPanel, BorderLayout.SOUTH);

        // --- EVENT LISTENERS ---
        admitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                admitPatient();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPatientData();
            }
        });

        loadPatientData();
        setLocationRelativeTo(null);
    }

    private void admitPatient() {
        String name = nameField.getText();
        String ageStr = ageField.getText();
        String ailment = ailmentField.getText();

        if (name.isEmpty() || ageStr.isEmpty() || ailment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO patients (patient_name, age, ailment) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(ageStr));
            pstmt.setString(3, ailment);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient Admitted Successfully!");

            nameField.setText("");
            ageField.setText("");
            ailmentField.setText("");
            loadPatientData();

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPatientData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "SELECT * FROM patients ORDER BY arrival_time ASC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("patient_name");
                int age = rs.getInt("age");
                String ailment = rs.getString("ailment");
                String arrival = rs.getString("arrival_time");

                tableModel.addRow(new Object[]{id, name, age, ailment, arrival});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new HospitalGUI().setVisible(true);
        });
    }
}
