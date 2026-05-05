package Features;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.function.BiConsumer;

public class RBI extends JPanel {

    private static final String DATA_FILE = "src/RBI/RBIdata.dat";

    private Color color1 = new Color(204, 218, 227);
    private Color listBgColor = Color.WHITE;
    private Font titleFont = new Font("Arial", Font.BOLD, 40);
    private Font labelFont = new Font("Arial", Font.PLAIN, 14);
    private Font inputFont = new Font("Arial", Font.PLAIN, 14);

    private JPanel cardContainer;
    private CardLayout cardLayout;
    private JPanel listPanel;

    // Table components
    private JTable residenceTable;
    private DefaultTableModel tableModel;
    private JScrollPane residenceScroll;

    private JButton btnNewResidence;
    private JButton btnDeleteResidence;
    private JButton btnRefresh;

    private JPanel formPanel;
    private JScrollPane formScrollPane;

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
        buildFormPanel();

        cardContainer.add(listPanel, "LIST");
        cardContainer.add(formScrollPane, "FORM");

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);

        updateResidenceLists();
    }

    private void buildListPanel() {
        listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(color1);

        JLabel title = new JLabel("<html><center>Barangay Inhabitant<br>" +
                "Profiling System</center></html>");
        title.setFont(titleFont);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(0, 100));
        title.setOpaque(true);
        title.setBackground(color1);

        // --- TABLE SETUP ---
        String[] columns = {"First Name", "Last Name", "Sex", "Civil Status", "Address", "Contact Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        residenceTable = new JTable(tableModel);

        // CRITICAL CHANGES FOR FILLING SPACE:
        // 1. Disable auto-resize to allow manual control or full-width distribution
        residenceTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        // OR use AUTO_RESIZE_ALL_COLUMNS to force equal width, but SUBSEQUENT is usually better for dynamic data

        // 2. Ensure the table fills the viewport height
        residenceTable.setFillsViewportHeight(true);

        // 3. Set row height for readability
        residenceTable.setRowHeight(32);

        // Styling
        residenceTable.setFont(new Font("Arial", Font.PLAIN, 12));
        residenceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        residenceTable.setSelectionBackground(new Color(180, 210, 255));

        // Center align specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        residenceTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Sex
        residenceTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Civil Status

        // Optional: Add a component listener to resize columns when the panel is resized
        residenceTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = residenceTable.getWidth();
                int columnCount = residenceTable.getColumnCount();
                int colWidth = width / columnCount;

                // Set all columns to equal width (optional, remove if you prefer variable widths)
                for (int i = 0; i < columnCount; i++) {
                    residenceTable.getColumnModel().getColumn(i).setWidth(colWidth);
                    residenceTable.getColumnModel().getColumn(i).setPreferredWidth(colWidth);
                }
            }
        });

        residenceScroll = new JScrollPane(residenceTable);
        residenceScroll.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        residenceScroll.setBackground(listBgColor);
        // Ensure scroll pane fills the remaining space in the BorderLayout
        residenceScroll.setPreferredSize(new Dimension(0, 0));
        // --- END TABLE SETUP ---

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(color1);

        btnNewResidence = new JButton("New Resident");
        btnNewResidence.setFont(new Font("Arial", Font.BOLD, 16));
        btnNewResidence.setBackground(new Color(80, 200, 80));
        btnNewResidence.setForeground(Color.WHITE);
        btnNewResidence.setFocusPainted(false);
        btnNewResidence.setPreferredSize(new Dimension(200, 50));
        btnNewResidence.addActionListener(e -> {
            cardLayout.show(cardContainer, "FORM");
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
        listPanel.add(residenceScroll, BorderLayout.CENTER);
        listPanel.add(btnPanel, BorderLayout.SOUTH);
    }
    private void buildFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(color1);
        formPanel.setPreferredSize(new Dimension(650, 1200));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

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
            if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).setFont(inputFont);
            }

            formPanel.add(comp, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        };

        // --- PERSONAL INFO ---
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel persHeader = new JLabel("<html><b>Personal Information</b></html>");
        persHeader.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(persHeader, gbc);
        gbc.gridwidth = 1;
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

        // --- IDENTITY INFO ---
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

        // --- CONTACT INFO ---
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

        formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
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

            writeField.accept("First Name", tfFirstName.getText());
            writeField.accept("Middle Name", tfMiddleName.getText());
            writeField.accept("Last Name", tfLastName.getText());
            writeField.accept("Extension Name", tfExtensionName.getText());
            writeField.accept("Sex", (String) cbSex.getSelectedItem());
            writeField.accept("Civil Status", (String) cbCivilStatus.getSelectedItem());
            writeField.accept("Date of Birth", tfBirthDate.getText());
            writeField.accept("Place of Birth", tfPlaceOfBirth.getText());
            writeField.accept("Resident Voter", (String) cbResidentVoter.getSelectedItem());
            writeField.accept("Citizenship", tfCitizenship.getText());
            writeField.accept("Occupation", tfOccupation.getText());
            writeField.accept("Religion", tfReligion.getText());
            writeField.accept("Sectoral Group", (String) cbSectoralGroup.getSelectedItem());
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

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean collecting = false;
            StringBuilder block = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("First Name") && line.contains(nameToDelete)) {
                    collecting = true;
                    block.append(line).append("\n");
                } else if (collecting) {
                    block.append(line).append("\n");
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
                if (skipMode) {
                    if (line.trim().equals("--------------------------------------------------")) {
                        skipMode = false;
                        continue;
                    }
                    continue;
                }

                if (line.startsWith("First Name") && line.contains(nameToDelete)) {
                    skipMode = true;
                    continue;
                }

                newLines.add(line);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
                for (String l : newLines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

            updateResidenceLists();
            JOptionPane.showMessageDialog(this, "Deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateResidenceLists() {
        tableModel.setRowCount(0); // Clear existing rows
        File file = new File(DATA_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String firstName = "", lastName = "", sex = "", civilStatus = "", contact = "";
            String houseNo = "", street = "", subdivision = "";
            boolean inRecord = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("First Name:")) {
                    inRecord = true;
                    firstName = line.replace("First Name:", "").trim();
                    lastName = ""; sex = ""; civilStatus = ""; contact = "";
                    houseNo = ""; street = "";                     subdivision = "";
                } else if (inRecord) {
                    if (line.startsWith("Last Name:")) {
                        lastName = line.replace("Last Name:", "").trim();
                    } else if (line.startsWith("Sex:")) {
                        sex = line.replace("Sex:", "").trim();
                    } else if (line.startsWith("Civil Status:")) {
                        civilStatus = line.replace("Civil Status:", "").trim();
                    } else if (line.startsWith("House No.")) {
                        houseNo = line.replace("House No.:", "").trim();
                    } else if (line.startsWith("Street Name:")) {
                        street = line.replace("Street Name:", "").trim();
                    } else if (line.startsWith("Subdivision/Zone")) {
                        subdivision = line.replace("Subdivision/Zone:", "").trim();
                    } else if (line.startsWith("Phone Number:")) {
                        contact = line.replace("Phone Number:", "").trim();
                    } else if (line.trim().equals("--------------------------------------------------")) {
                        // End of record reached, build the address and add row
                        StringBuilder sb = new StringBuilder();
                        if (!houseNo.isEmpty()) sb.append(houseNo);
                        if (!street.isEmpty()) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(street);
                        }
                        if (!subdivision.isEmpty()) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(subdivision);
                        }

                        String fullAddress = sb.toString();
                        if (fullAddress.isEmpty()) fullAddress = "N/A";

                        // Add row to table
                        // Columns: First Name, Last Name, Sex, Civil Status, Address, Contact
                        tableModel.addRow(new String[]{
                                firstName,
                                lastName,
                                sex,
                                civilStatus,
                                fullAddress,
                                contact
                        });

                        // Reset flags
                        inRecord = false;
                        firstName = "";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading data file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        tfFirstName.setText("");
        tfMiddleName.setText("");
        tfLastName.setText("");
        tfExtensionName.setText("");
        cbSex.setSelectedIndex(0);
        cbCivilStatus.setSelectedIndex(0);
        tfBirthDate.setText("");
        tfPlaceOfBirth.setText("");

        cbResidentVoter.setSelectedIndex(0);
        tfCitizenship.setText("");
        tfOccupation.setText("");
        tfReligion.setText("");
        cbSectoralGroup.setSelectedIndex(0);

        tfEmail.setText("");
        tfContact.setText("");
        tfHouseNo.setText("");
        tfStreet.setText("");
        tfSubdivision.setText("");
        tfHouseholdNum.setText("");
    }
}