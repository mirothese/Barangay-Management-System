package Features;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class RBI extends JPanel {

    // --- Configuration ---
    private static final String DATA_FILE = "src/RBI/RBIdata.dat";

    // --- UI Components ---
    private Color color1 = new Color(204, 218, 227);
    private Color listBgColor = Color.WHITE;
    private Font titleFont = new Font("Arial", Font.BOLD, 40);
    private Font labelFont = new Font("Arial", Font.PLAIN, 14);
    private Font inputFont = new Font("Arial", Font.PLAIN, 14);

    private JPanel cardContainer;
    private CardLayout cardLayout;
    private JPanel listPanel;
    private JTextArea residences;
    private JScrollPane residenceList;
    private JButton btnNewResidence;
    private JButton btnDeleteResidence;
    private JButton btnRefresh;

    private JPanel formPanel;
    private JScrollPane formScrollPane; // New scroll pane for the form

    // Personal Info
    private JTextField tfFirstName, tfMiddleName, tfLastName, tfExtensionName;
    private JComboBox<String> cbSex;
    private JComboBox<String> cbCivilStatus;
    private JTextField tfBirthDate, tfPlaceOfBirth;

    // Identity
    private JComboBox<String> cbResidentVoter;
    private JTextField tfCitizenship, tfReligion;
    private JComboBox<String> cbSectoralGroup;
    private JTextField tfOccupation;

    // Contact
    private JTextField tfEmail, tfContact;
    private JTextField tfHouseNo, tfStreet, tfSubdivision, tfHouseholdNum;

    private JButton btnSave, btnCancel;

    public RBI() {
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(color1);

        buildListPanel();
        buildFormPanel(); // This now creates the scrollable form

        cardContainer.add(listPanel, "LIST");
        cardContainer.add(formScrollPane, "FORM"); // Add the scroll pane, not the raw panel

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);

        updateResidenceLists();
    }

    private void buildListPanel() {
        listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(color1);

        JLabel title = new JLabel("<html><center>Residence of<br>Barangay Inhabitants</center></html>");
        title.setFont(titleFont);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(0, 100));
        title.setOpaque(true);
        title.setBackground(color1);

        residences = new JTextArea();
        residences.setFont(new Font("Arial", Font.PLAIN, 12));
        residences.setLineWrap(true);
        residences.setEditable(false);
        residences.setOpaque(true);
        residences.setBackground(listBgColor);
        residences.setForeground(Color.BLACK);

        residenceList = new JScrollPane(residences);
        residenceList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        residenceList.setBackground(listBgColor);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        btnPanel.setBackground(color1);

        btnNewResidence = new JButton("New Resident");
        btnNewResidence.setFont(new Font("Arial", Font.BOLD, 16));
        btnNewResidence.setBackground(new Color(80, 200, 80));
        btnNewResidence.setForeground(Color.WHITE);
        btnNewResidence.setFocusPainted(false);
        btnNewResidence.setPreferredSize(new Dimension(200, 50));
        btnNewResidence.addActionListener(e -> {
            cardLayout.show(cardContainer, "FORM");
            // Reset scroll position when opening form
            formScrollPane.getVerticalScrollBar().setValue(0);
            cardContainer.revalidate();
            cardContainer.repaint();
            clearForm();
            tfFirstName.requestFocus();
        });

        btnDeleteResidence = new JButton("Delete Resident");
        btnDeleteResidence.setFont(new Font("Arial", Font.BOLD, 16));
        btnDeleteResidence.setPreferredSize(new Dimension(200, 50));
        btnDeleteResidence.setBackground(new Color(230, 80, 80));
        btnDeleteResidence.setForeground(Color.WHITE);
        btnDeleteResidence.setFocusPainted(false);
        btnDeleteResidence.addActionListener(e -> deleteResidentByName());

        btnRefresh = new JButton("Refresh List");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(80, 80, 230));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> updateResidenceLists());

        btnPanel.add(btnNewResidence);
        btnPanel.add(btnDeleteResidence);
        btnPanel.add(btnRefresh);

        listPanel.add(title, BorderLayout.NORTH);
        listPanel.add(residenceList, BorderLayout.CENTER);
        listPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void buildFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(color1);
        // Set a minimum width to ensure layout is consistent
        formPanel.setPreferredSize(new Dimension(650, 1200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Helper to add label + field
        BiConsumer<String, Component> addComponent = (labelText, comp) -> {
            JLabel label = new JLabel(labelText + ":");
            label.setFont(labelFont);
            gbc.gridx = 0;
            formPanel.add(label, gbc);
            gbc.gridx = 1;

            if (comp instanceof JTextField) {
                ((JTextField) comp).setFont(inputFont);
                ((JTextField) comp).setPreferredSize(new Dimension(300, 30));
            }
            // For JComboBox, set preferred size if needed, but usually it adapts
            if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setFont(inputFont);
            }

            formPanel.add(comp, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        };

        // --- HEADER: LOCATION ---
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel locHeader = new JLabel("<html><b>Location Information</b></html>");
        locHeader.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(locHeader, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;

        // --- HEADER: PERSONAL INFO ---
        gbc.gridy++;
        JLabel persHeader = new JLabel("<html><b>Personal Information</b></html>");
        persHeader.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(persHeader, gbc);
        gbc.gridy++;

        tfFirstName = new JTextField();
        tfMiddleName = new JTextField();
        tfLastName = new JTextField();
        tfExtensionName = new JTextField();

        cbSex = new JComboBox<>(new String[]{"MALE", "FEMALE"});
        cbCivilStatus = new JComboBox<>(new String[]{"SINGLE", "MARRIED", "WIDOW/ER", "SEPARATED", "SIDECHICK"});

        tfBirthDate = new JTextField();
        tfPlaceOfBirth = new JTextField();

        addComponent.accept("First Name", tfFirstName);
        addComponent.accept("Middle Name", tfMiddleName);
        addComponent.accept("Last Name", tfLastName);
        addComponent.accept("Extension Name", tfExtensionName);
        addComponent.accept("Sex", cbSex);
        addComponent.accept("Civil Status", cbCivilStatus);
        addComponent.accept("Date of Birth", tfBirthDate);
        addComponent.accept("Place of Birth", tfPlaceOfBirth);

        // --- HEADER: IDENTITY INFO ---
        gbc.gridy++;
        JLabel idHeader = new JLabel("<html><b>Identity Information</b></html>");
        idHeader.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(idHeader, gbc);
        gbc.gridy++;

        cbResidentVoter = new JComboBox<>(new String[]{"YES", "NO"});
        tfCitizenship = new JTextField();
        tfOccupation = new JTextField();
        tfReligion = new JTextField();

        String[] sectors = {"None", "Solo Parent", "OFW", "PWD", "OSC", "OSY", "Unemployed", "Labor Force", "ISY", "4Ps", "Senior Citizen"};
        cbSectoralGroup = new JComboBox<>(sectors);

        addComponent.accept("Resident Voter", cbResidentVoter);
        addComponent.accept("Citizenship", tfCitizenship);
        addComponent.accept("Profession/Occupation", tfOccupation);
        addComponent.accept("Religion", tfReligion);
        addComponent.accept("Sectoral Group", cbSectoralGroup);

        // --- HEADER: CONTACT INFO ---
        gbc.gridy++;
        JLabel conHeader = new JLabel("<html><b>Contact Information</b></html>");
        conHeader.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(conHeader, gbc);
        gbc.gridy++;

        tfEmail = new JTextField();
        tfContact = new JTextField();
        tfHouseNo = new JTextField();
        tfStreet = new JTextField();
        tfSubdivision = new JTextField();
        tfHouseholdNum = new JTextField();

        addComponent.accept("Email", tfEmail);
        addComponent.accept("Phone Number", tfContact);
        addComponent.accept("House No.", tfHouseNo);
        addComponent.accept("Street Name", tfStreet);
        addComponent.accept("Subdivision/Zone/Sitio/Purok", tfSubdivision);
        addComponent.accept("Household Number", tfHouseholdNum);

        // --- BUTTONS ---
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnRow.setBackground(color1);

        btnSave = new JButton("Save Resident");
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.addActionListener(e -> saveResident());

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.addActionListener(e -> {
            cardLayout.show(cardContainer, "LIST");
            cardContainer.revalidate();
            cardContainer.repaint();
        });

        btnRow.add(btnSave);
        btnRow.add(btnCancel);

        formPanel.add(btnRow, gbc);

        // Wrap formPanel in a JScrollPane
        formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border for cleaner look
        formScrollPane.setBackground(color1);
    }

    private void saveResident() {
        if (tfFirstName.getText().isEmpty() || tfLastName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First and Last Name are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedWriter RBIdata = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            BiConsumer<String, String> writeField = (label, val) -> {
                try {
                    RBIdata.write(label + ": " + val + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            // Personal
            writeField.accept("First Name", tfFirstName.getText());
            writeField.accept("Middle Name", tfMiddleName.getText());
            writeField.accept("Last Name", tfLastName.getText());
            writeField.accept("Extension Name", tfExtensionName.getText());
            writeField.accept("Sex", (String) cbSex.getSelectedItem());
            writeField.accept("Civil Status", (String) cbCivilStatus.getSelectedItem());
            writeField.accept("Date of Birth", tfBirthDate.getText());
            writeField.accept("Place of Birth", tfPlaceOfBirth.getText());

            // Identity
            writeField.accept("Resident Voter", (String) cbResidentVoter.getSelectedItem());
            writeField.accept("Citizenship", tfCitizenship.getText());
            writeField.accept("Occupation", tfOccupation.getText());
            writeField.accept("Religion", tfReligion.getText());
            writeField.accept("Sectoral Group", (String) cbSectoralGroup.getSelectedItem());

            // Contact
            writeField.accept("Email", tfEmail.getText());
            writeField.accept("Phone Number", tfContact.getText());
            writeField.accept("House No.", tfHouseNo.getText());
            writeField.accept("Street Name", tfStreet.getText());
            writeField.accept("Subdivision/Zone", tfSubdivision.getText());
            writeField.accept("Household Number", tfHouseholdNum.getText());

            RBIdata.write("--------------------------------------------------\n");

            clearForm();
            cardLayout.show(cardContainer, "LIST");
            listPanel.revalidate();
            listPanel.repaint();
            cardContainer.revalidate();
            cardContainer.repaint();

            JOptionPane.showMessageDialog(this, "Resident added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error saving resident.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        updateResidenceLists();
    }

    private void deleteResidentByName() {
        String nameToDelete = JOptionPane.showInputDialog(this, "Enter First Name to delete:", "Delete", JOptionPane.QUESTION_MESSAGE);
        if (nameToDelete == null || nameToDelete.trim().isEmpty()) return;
        nameToDelete = nameToDelete.trim();

        File file = new File(DATA_FILE);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "File not found: " + DATA_FILE, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fullRecord = "";
        boolean found = false;

        // 1. Read file to find the block to delete
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean collecting = false;
            StringBuilder block = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // Check if line starts with "First Name" and contains the target name
                if (line.startsWith("First Name") && line.contains(nameToDelete)) {
                    collecting = true;
                    block.append(line).append("\n");
                } else if (collecting) {
                    block.append(line).append("\n");
                    // FIX: Trim the line to remove any accidental trailing spaces/newlines
                    if (line.trim().equals("--------------------------------------------------")) {
                        found = true;
                        fullRecord = block.toString();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Name not found: " + nameToDelete, "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Delete this record?\n" + fullRecord, "Confirm Deletion", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        // 2. Rewrite the file, skipping the found block
        try {
            List<String> allLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    allLines.add(line);
                }
            }

            List<String> newLines = new ArrayList<>();
            boolean skipMode = false;

            for (String line : allLines) {
                // If we are currently skipping a block
                if (skipMode) {
                    // FIX: Trim the line to check for separator safely
                    if (line.trim().equals("--------------------------------------------------")) {
                        skipMode = false;
                        // Do NOT add the separator line to the new list
                        continue;
                    }
                    // If still skipping, do not add the line
                    continue;
                }

                // Check if this line starts the block to delete
                if (line.startsWith("First Name") && line.contains(nameToDelete)) {
                    skipMode = true;
                    continue; // Skip this line too
                }

                // If not skipping, add to new list
                newLines.add(line);
            }

            // Write the clean list back
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
                for (String l : newLines) {
                    writer.write(l);
                    writer.newLine();
                }
                // Explicitly flush and close (handled by try-with-resources, but good to be sure)
                writer.flush();
            }

            // CRITICAL: Force the UI to reload immediately
            updateResidenceLists();

            JOptionPane.showMessageDialog(this, "Deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateResidenceLists() {
        residences.setText("");
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            residences.setText("No records found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                residences.append(line + "\n");
            }
        } catch (IOException e) {
            residences.setText("Error reading file.\n" + e.getMessage());
        }

        residences.setCaretPosition(0);
        residences.revalidate();
        residences.repaint();
    }

    private void clearForm() {

        // Personal Info
        tfFirstName.setText("");
        tfMiddleName.setText("");
        tfLastName.setText("");
        tfExtensionName.setText("");
        cbSex.setSelectedIndex(0); // MALE
        cbCivilStatus.setSelectedIndex(0); // SINGLE
        tfBirthDate.setText("");
        tfPlaceOfBirth.setText("");

        // Identity
        cbResidentVoter.setSelectedIndex(0); // YES
        tfCitizenship.setText("");
        tfOccupation.setText("");
        tfReligion.setText("");
        cbSectoralGroup.setSelectedIndex(0); // None

        // Contact
        tfEmail.setText("");
        tfContact.setText("");
        tfHouseNo.setText("");
        tfStreet.setText("");
        tfSubdivision.setText("");
        tfHouseholdNum.setText("");
    }
}