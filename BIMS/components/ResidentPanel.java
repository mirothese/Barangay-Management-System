package components;

import model.Resident;
import dao.ResidentDAO;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.List;

public class ResidentPanel extends JPanel {
    private ResidentDAO dao;
    private JTable residentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchFilter;
    
    // Form fields
    private JDialog formDialog;
    private JTextField lastNameField, firstNameField, middleNameField, extensionField;
    private JTextField placeOfBirthField, residenceAddressField;
    private JTextField phoneField, emailField, occupationField, religionField, citizenshipField;
    
    // Dropdowns
    private JComboBox<String> regionBox, provinceBox, cityBox, barangayBox;
    private JComboBox<String> sexBox, civilStatusBox, sectoralBox;
    
    // Radio buttons for Voter
    private JRadioButton voterYes, voterNo;
    private ButtonGroup voterGroup;
    
    // Age range for search
    private JTextField ageMinField, ageMaxField;

    // Date fields
    private JSpinner daySpinner, monthSpinner, yearSpinner;
    
    private boolean isEditMode = false;
    private Resident editingResident;
    
    // Colors
    private final Color BG_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(33, 37, 41);
    private final Color HEADER_BG = new Color(240, 240, 240);
    
    public ResidentPanel() {
        dao = new ResidentDAO();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        initUI();
        loadTableData();
    }
    
    private void initUI() {
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "SEARCH RESIDENTS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(33, 37, 41)
        ));
        
        JLabel searchByLabel = new JLabel("Search by:");
        searchByLabel.setForeground(new Color(33, 37, 41));
        panel.add(searchByLabel);
        
        // Updated search filter options
        searchFilter = new JComboBox<>(new String[]{"Last Name", "First Name", "Age", "Voter", "Sectoral Group"});
        panel.add(searchFilter);
        
        searchField = new JTextField(15);
        panel.add(searchField);
        
        // Age range panel (hidden by default, shown when Age is selected)
        JPanel ageRangePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ageRangePanel.setBackground(Color.WHITE);
        ageMinField = new JTextField(3);
        ageMaxField = new JTextField(3);
        ageRangePanel.add(new JLabel("Min:"));
        ageRangePanel.add(ageMinField);
        ageRangePanel.add(new JLabel("Max:"));
        ageRangePanel.add(ageMaxField);
        ageRangePanel.setVisible(false);
        panel.add(ageRangePanel);
        
        // Show/hide age range panel based on selection
        searchFilter.addActionListener(e -> {
            String selected = (String) searchFilter.getSelectedItem();
            if ("Age".equals(selected)) {
                searchField.setVisible(false);
                ageRangePanel.setVisible(true);
            } else {
                searchField.setVisible(true);
                ageRangePanel.setVisible(false);
                ageMinField.setText("");
                ageMaxField.setText("");
            }
            panel.revalidate();
            panel.repaint();
        });
        
        JButton searchBtn = createStyledButton("SEARCH", new Color(52, 152, 219));
        searchBtn.addActionListener(e -> searchResidents());
        panel.add(searchBtn);
        
        JButton refreshBtn = createStyledButton("REFRESH", new Color(46, 204, 113));
        refreshBtn.addActionListener(e -> loadTableData());
        panel.add(refreshBtn);
        
        JButton clearSearchBtn = createStyledButton("CLEAR", new Color(155, 89, 182));
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadTableData();
        });
        panel.add(clearSearchBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "RESIDENT RECORDS",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(33, 37, 41)
        ));
        
        String[] columns = {"Last Name", "First Name", "Middle Name", "Age", "Sex", "Civil Status", 
                           "Occupation", "Barangay", "Voter", "Sectoral Group", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        residentTable = new JTable(tableModel);
        residentTable.setBackground(Color.WHITE);
        residentTable.setForeground(new Color(33, 37, 41));
        residentTable.setGridColor(new Color(200, 200, 200));
        residentTable.setSelectionBackground(new Color(13, 110, 253));
        residentTable.setSelectionForeground(Color.WHITE);
        residentTable.setRowHeight(25);
        residentTable.getTableHeader().setBackground(HEADER_BG);
        residentTable.getTableHeader().setForeground(new Color(33, 37, 41));
        residentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(residentTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton addBtn = createStyledButton("ADD NEW RESIDENT", new Color(46, 204, 113));
        JButton editBtn = createStyledButton("EDIT SELECTED", new Color(52, 152, 219));
        JButton deleteBtn = createStyledButton("DELETE SELECTED", new Color(231, 76, 60));
        JButton viewBtn = createStyledButton("VIEW DETAILS", new Color(155, 89, 182));
        JButton statsBtn = createStyledButton("VIEW STATISTICS", new Color(230, 126, 34));
        
        addBtn.addActionListener(e -> showAddForm());
        editBtn.addActionListener(e -> editSelectedResident());
        deleteBtn.addActionListener(e -> deleteSelectedResident());
        viewBtn.addActionListener(e -> viewSelectedResident());
        statsBtn.addActionListener(e -> showStatistics());
        
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(viewBtn);
        panel.add(statsBtn);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
    
    private JTextField createUpperCaseTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isLowerCase(c)) {
                    e.setKeyChar(Character.toUpperCase(c));
                }
            }
        });
        return field;
    }
    
    private void showAddForm() {
        isEditMode = false;
        editingResident = null;
        createFormDialog("ADD NEW RESIDENT");
    }
    
    private void editSelectedResident() {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String lastName = (String) tableModel.getValueAt(selectedRow, 0);
            String firstName = (String) tableModel.getValueAt(selectedRow, 1);
            editingResident = dao.findByFullName(lastName, firstName);
            if (editingResident != null) {
                isEditMode = true;
                createFormDialog("EDIT RESIDENT");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedResident() {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String lastName = (String) tableModel.getValueAt(selectedRow, 0);
            String firstName = (String) tableModel.getValueAt(selectedRow, 1);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete " + lastName + ", " + firstName + "?\nThis action cannot be undone.", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteInhabitant(lastName, firstName);
                loadTableData();
                JOptionPane.showMessageDialog(this, "Resident deleted successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void viewSelectedResident() {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String lastName = (String) tableModel.getValueAt(selectedRow, 0);
            String firstName = (String) tableModel.getValueAt(selectedRow, 1);
            Resident resident = dao.findByFullName(lastName, firstName);
            if (resident != null) {
                showViewDialog(resident);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showViewDialog(Resident resident) {
        JDialog viewDialog = new JDialog();
        viewDialog.setTitle("RESIDENT DETAILS");
        viewDialog.setSize(600, 700);
        viewDialog.setLocationRelativeTo(this);
        viewDialog.setModal(true);
        viewDialog.getContentPane().setBackground(Color.WHITE);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(new Color(33, 37, 41));
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder details = new StringBuilder();
        details.append("=".repeat(55)).append("\n");
        details.append("RESIDENT INFORMATION\n");
        details.append("=".repeat(55)).append("\n\n");
        
        details.append("PERSONAL INFORMATION:\n");
        details.append("  Name: ").append(resident.getLastName()).append(", ").append(resident.getFirstName()).append("\n");
        details.append("  Middle Name: ").append(resident.getMiddleName()).append("\n");
        details.append("  Extension: ").append(resident.getExtensionName()).append("\n");
        details.append("  Age: ").append(resident.getAge()).append("\n");
        details.append("  Sex: ").append(resident.getSex()).append("\n");
        details.append("  Civil Status: ").append(resident.getCivilStatus()).append("\n");
        details.append("  Birth Date: ").append(resident.getBirthDate()).append("\n");
        details.append("  Place of Birth: ").append(resident.getPlaceOfBirth()).append("\n\n");
        
        details.append("LOCATION:\n");
        details.append("  Region: ").append(resident.getRegion()).append("\n");
        details.append("  Province: ").append(resident.getProvince()).append("\n");
        details.append("  City/Municipality: ").append(resident.getCityMunicipality()).append("\n");
        details.append("  Barangay: ").append(resident.getBarangay()).append("\n");
        details.append("  Address: ").append(resident.getResidenceAddress()).append("\n\n");
        
        details.append("CONTACT:\n");
        details.append("  Phone: ").append(resident.getPhoneNumber()).append("\n");
        details.append("  Email: ").append(resident.getEmail()).append("\n\n");
        
        details.append("ADDITIONAL INFO:\n");
        details.append("  Occupation: ").append(resident.getProfessionOccupation()).append("\n");
        details.append("  Religion: ").append(resident.getReligion()).append("\n");
        details.append("  Citizenship: ").append(resident.getCitizenship()).append("\n");
        details.append("  Sectoral Group: ").append(resident.getSectoralGroup()).append("\n");
        details.append("  Registered Voter: ").append(resident.getResidentVoter()).append("\n");
        
        textArea.setText(details.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        viewDialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeBtn = createStyledButton("CLOSE", new Color(149, 165, 166));
        closeBtn.addActionListener(e -> viewDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(closeBtn);
        viewDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        viewDialog.setVisible(true);
    }
    
    private void showStatistics() {
        JDialog statDialog = new JDialog();
        statDialog.setTitle("RESIDENT STATISTICS");
        statDialog.setSize(550, 500);
        statDialog.setLocationRelativeTo(this);
        statDialog.setModal(true);
        statDialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int total = dao.getTotalResidents();
        int male = dao.getMaleCount();
        int female = dao.getFemaleCount();
        int voters = dao.getVoterCount();
        int seniors = dao.getSeniorCitizenCount();
        int youth = dao.getYouthCount();
        int children = dao.getChildrenCount();
        int pwd = dao.getCountBySector("PWD");
        int ofw = dao.getCountBySector("OFW");
        int soloParent = dao.getCountBySector("SOLO PARENT");
        
        addStatRow(panel, "TOTAL RESIDENTS:", String.valueOf(total), gbc, 0);
        addStatRow(panel, "MALE:", String.valueOf(male), gbc, 1);
        addStatRow(panel, "FEMALE:", String.valueOf(female), gbc, 2);
        addStatRow(panel, "REGISTERED VOTERS:", String.valueOf(voters), gbc, 3);
        addStatRow(panel, "CHILDREN (0-14):", String.valueOf(children), gbc, 4);
        addStatRow(panel, "YOUTH (15-30):", String.valueOf(youth), gbc, 5);
        addStatRow(panel, "SENIOR CITIZENS (60+):", String.valueOf(seniors), gbc, 6);
        addStatRow(panel, "PWD:", String.valueOf(pwd), gbc, 7);
        addStatRow(panel, "OFW:", String.valueOf(ofw), gbc, 8);
        addStatRow(panel, "SOLO PARENTS:", String.valueOf(soloParent), gbc, 9);
        
        JButton closeBtn = createStyledButton("CLOSE", new Color(149, 165, 166));
        closeBtn.addActionListener(e -> statDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(closeBtn);
        
        statDialog.add(panel, BorderLayout.CENTER);
        statDialog.add(buttonPanel, BorderLayout.SOUTH);
        statDialog.setVisible(true);
    }
    
    private void addStatRow(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel labelLabel = new JLabel(label);
        labelLabel.setForeground(new Color(33, 37, 41));
        labelLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(labelLabel, gbc);
        
        gbc.gridx = 1;
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setForeground(new Color(13, 110, 253));
        panel.add(valueLabel, gbc);
    }
    
    private void addSectionHeader(JPanel panel, String title, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel header = new JLabel(title);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setForeground(new Color(13, 110, 253));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        panel.add(header, gbc);
        gbc.gridwidth = 1;
    }
    
    private void addField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel labelComp = new JLabel(label);
        labelComp.setForeground(new Color(33, 37, 41));
        panel.add(labelComp, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(field, gbc);
        
        if (field instanceof JTextField) {
            ((JTextField) field).setBackground(Color.WHITE);
            ((JTextField) field).setForeground(new Color(33, 37, 41));
            ((JTextField) field).setCaretColor(new Color(33, 37, 41));
        }
        if (field instanceof JComboBox) {
            ((JComboBox) field).setBackground(Color.WHITE);
            ((JComboBox) field).setForeground(new Color(33, 37, 41));
        }
    }
    
    private void createFormDialog(String title) {
        formDialog = new JDialog();
        formDialog.setTitle(title);
        formDialog.setSize(750, 850);
        formDialog.setLocationRelativeTo(this);
        formDialog.setLayout(new BorderLayout());
        formDialog.setModal(true);
        formDialog.getContentPane().setBackground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // PERSONAL INFORMATION
        addSectionHeader(formPanel, "PERSONAL INFORMATION", gbc, row++);
        
        lastNameField = createUpperCaseTextField(20);
        firstNameField = createUpperCaseTextField(20);
        middleNameField = createUpperCaseTextField(20);
        extensionField = createUpperCaseTextField(20);
        extensionField.setText("N/A");
        
        addField(formPanel, "Last Name:", lastNameField, gbc, row++);
        addField(formPanel, "First Name:", firstNameField, gbc, row++);
        addField(formPanel, "Middle Name:", middleNameField, gbc, row++);
        addField(formPanel, "Extension Name (Jr., Sr., III):", extensionField, gbc, row++);
        
        sexBox = new JComboBox<>(new String[]{"MALE", "FEMALE"});
        addField(formPanel, "Sex:", sexBox, gbc, row++);
        
        civilStatusBox = new JComboBox<>(new String[]{"SINGLE", "MARRIED", "WIDOW/ER", "SEPARATED"});
        addField(formPanel, "Civil Status:", civilStatusBox, gbc, row++);
        
        // Date of Birth
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setBackground(Color.WHITE);
        yearSpinner = new JSpinner(new SpinnerNumberModel(1990, 1900, 2024, 1));
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        
        JLabel yearLabel = new JLabel("Year:");
        JLabel monthLabel = new JLabel("Month:");
        JLabel dayLabel = new JLabel("Day:");
        
        dobPanel.add(yearLabel); dobPanel.add(yearSpinner);
        dobPanel.add(monthLabel); dobPanel.add(monthSpinner);
        dobPanel.add(dayLabel); dobPanel.add(daySpinner);
        addField(formPanel, "Date of Birth:", dobPanel, gbc, row++);
        
        placeOfBirthField = createUpperCaseTextField(20);
        addField(formPanel, "Place of Birth:", placeOfBirthField, gbc, row++);
        
        // LOCATION INFORMATION
        addSectionHeader(formPanel, "LOCATION INFORMATION", gbc, row++);
        
        regionBox = new JComboBox<>(ResidentDAO.REGIONS);
        regionBox.addActionListener(e -> updateProvinceBox());
        addField(formPanel, "Region:", regionBox, gbc, row++);
        
        provinceBox = new JComboBox<>(new String[]{"SELECT REGION FIRST"});
        provinceBox.addActionListener(e -> updateCityBox());
        addField(formPanel, "Province:", provinceBox, gbc, row++);
        
        cityBox = new JComboBox<>(new String[]{"SELECT PROVINCE FIRST"});
        cityBox.addActionListener(e -> updateBarangayBox());
        addField(formPanel, "City/Municipality:", cityBox, gbc, row++);
        
        barangayBox = new JComboBox<>(new String[]{"SELECT CITY FIRST"});
        addField(formPanel, "Barangay:", barangayBox, gbc, row++);
        
        residenceAddressField = createUpperCaseTextField(20);
        addField(formPanel, "Residence Address (House/Street):", residenceAddressField, gbc, row++);
        
        // CONTACT INFORMATION
        addSectionHeader(formPanel, "CONTACT INFORMATION", gbc, row++);
        
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        
        addField(formPanel, "Phone Number:", phoneField, gbc, row++);
        addField(formPanel, "Email Address:", emailField, gbc, row++);
        
        // ADDITIONAL INFORMATION
        addSectionHeader(formPanel, "ADDITIONAL INFORMATION", gbc, row++);
        
        occupationField = createUpperCaseTextField(20);
        religionField = createUpperCaseTextField(20);
        citizenshipField = createUpperCaseTextField(20);
        citizenshipField.setText("FILIPINO");
        
        sectoralBox = new JComboBox<>(new String[]{"NONE", "Solo Parent","OFW (Overseas Filipino Worker)","Senior Citizen (SC)", "PWD (Person with Disabilities)", "OSC (Out-of-School Child)", "OSY (Out-of-School Youth)", "ISY (In-School Youth)", "4Ps", "IP (Indigenous Person)"});
        
        // Radio buttons for Voter
        JPanel voterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        voterPanel.setBackground(Color.WHITE);
        voterYes = new JRadioButton("YES");
        voterNo = new JRadioButton("NO");
        voterGroup = new ButtonGroup();
        voterGroup.add(voterYes);
        voterGroup.add(voterNo);
        voterNo.setSelected(true);
        voterPanel.add(voterYes);
        voterPanel.add(voterNo);
        
        addField(formPanel, "Occupation:", occupationField, gbc, row++);
        addField(formPanel, "Religion:", religionField, gbc, row++);
        addField(formPanel, "Citizenship:", citizenshipField, gbc, row++);
        addField(formPanel, "Sectoral Group:", sectoralBox, gbc, row++);
        addField(formPanel, "Registered Voter:", voterPanel, gbc, row++);
        
        if (isEditMode && editingResident != null) {
            fillFormData();
        }
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(Color.WHITE);
        formDialog.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));
        JButton saveBtn = createStyledButton("SAVE", new Color(46, 204, 113));
        JButton cancelBtn = createStyledButton("CANCEL", new Color(149, 165, 166));
        
        saveBtn.addActionListener(e -> {
            saveResident();
            formDialog.dispose();
        });
        cancelBtn.addActionListener(e -> formDialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        formDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        formDialog.setVisible(true);
    }
    
    private void fillFormData() {
        lastNameField.setText(editingResident.getLastName());
        firstNameField.setText(editingResident.getFirstName());
        middleNameField.setText(editingResident.getMiddleName());
        extensionField.setText(editingResident.getExtensionName());
        
        sexBox.setSelectedItem(editingResident.getSex());
        civilStatusBox.setSelectedItem(editingResident.getCivilStatus());
        
        if (editingResident.getBirthDate() != null) {
            yearSpinner.setValue(editingResident.getBirthDate().getYear());
            monthSpinner.setValue(editingResident.getBirthDate().getMonthValue());
            daySpinner.setValue(editingResident.getBirthDate().getDayOfMonth());
        }
        
        placeOfBirthField.setText(editingResident.getPlaceOfBirth());
        
        regionBox.setSelectedItem(editingResident.getRegion());
        updateProvinceBox();
        provinceBox.setSelectedItem(editingResident.getProvince());
        updateCityBox();
        cityBox.setSelectedItem(editingResident.getCityMunicipality());
        updateBarangayBox();
        barangayBox.setSelectedItem(editingResident.getBarangay());
        
        residenceAddressField.setText(editingResident.getResidenceAddress());
        phoneField.setText(editingResident.getPhoneNumber());
        emailField.setText(editingResident.getEmail());
        occupationField.setText(editingResident.getProfessionOccupation());
        religionField.setText(editingResident.getReligion());
        citizenshipField.setText(editingResident.getCitizenship());
        sectoralBox.setSelectedItem(editingResident.getSectoralGroup());
        
        if ("YES".equals(editingResident.getResidentVoter())) {
            voterYes.setSelected(true);
        } else {
            voterNo.setSelected(true);
        }
    }
    
    private void saveResident() {
        try {
            Resident resident = new Resident();
            
            resident.setLastName(lastNameField.getText().toUpperCase());
            resident.setFirstName(firstNameField.getText().toUpperCase());
            resident.setMiddleName(middleNameField.getText().toUpperCase());
            resident.setExtensionName(extensionField.getText().toUpperCase());
            resident.setFullName();
            
            resident.setSex((String) sexBox.getSelectedItem());
            resident.setCivilStatus((String) civilStatusBox.getSelectedItem());
            
            int year = (Integer) yearSpinner.getValue();
            int month = (Integer) monthSpinner.getValue();
            int day = (Integer) daySpinner.getValue();
            resident.setBirthDate(LocalDate.of(year, month, day));
            
            resident.setPlaceOfBirth(placeOfBirthField.getText().toUpperCase());
            
            resident.setRegion((String) regionBox.getSelectedItem());
            resident.setProvince((String) provinceBox.getSelectedItem());
            resident.setCityMunicipality((String) cityBox.getSelectedItem());
            resident.setBarangay((String) barangayBox.getSelectedItem());
            
            resident.setResidenceAddress(residenceAddressField.getText().toUpperCase());
            resident.setPhoneNumber(phoneField.getText());
            resident.setEmail(emailField.getText().toLowerCase());
            resident.setProfessionOccupation(occupationField.getText().toUpperCase());
            resident.setReligion(religionField.getText().toUpperCase());
            resident.setCitizenship(citizenshipField.getText().toUpperCase());
            resident.setSectoralGroup((String) sectoralBox.getSelectedItem());
            resident.setResidentVoter(voterYes.isSelected() ? "YES" : "NO");
            
            if (lastNameField.getText().isEmpty() || firstNameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(formDialog, "Last Name and First Name are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isEditMode && editingResident != null) {
                dao.updateInhabitant(resident);
                JOptionPane.showMessageDialog(formDialog, "Resident updated successfully!");
            } else {
                dao.addInhabitant(resident);
                JOptionPane.showMessageDialog(formDialog, "Resident added successfully!");
            }
            
            loadTableData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(formDialog, "Error saving resident: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateProvinceBox() {
        String selectedRegion = (String) regionBox.getSelectedItem();
        if (selectedRegion != null && ResidentDAO.PROVINCE_MAP.containsKey(selectedRegion)) {
            provinceBox.removeAllItems();
            for (String province : ResidentDAO.PROVINCE_MAP.get(selectedRegion)) {
                provinceBox.addItem(province);
            }
            provinceBox.setEnabled(true);
            updateCityBox();
        } else {
            provinceBox.removeAllItems();
            provinceBox.addItem("SELECT PROVINCE");
            provinceBox.setEnabled(false);
            cityBox.setEnabled(false);
            barangayBox.setEnabled(false);
        }
    }
    
    private void updateCityBox() {
        String selectedProvince = (String) provinceBox.getSelectedItem();
        if (selectedProvince != null && ResidentDAO.CITY_MAP.containsKey(selectedProvince)) {
            cityBox.removeAllItems();
            for (String city : ResidentDAO.CITY_MAP.get(selectedProvince)) {
                cityBox.addItem(city);
            }
            cityBox.setEnabled(true);
            updateBarangayBox();
        } else {
            cityBox.removeAllItems();
            cityBox.addItem("SELECT CITY");
            cityBox.setEnabled(false);
            barangayBox.setEnabled(false);
        }
    }
    
    private void updateBarangayBox() {
        String selectedCity = (String) cityBox.getSelectedItem();
        if (selectedCity != null && selectedCity.equals("CITY OF LAOAG")) {
            barangayBox.removeAllItems();
            for (String barangay : ResidentDAO.BARANGAYS) {
                barangayBox.addItem(barangay);
            }
            barangayBox.setEnabled(true);
        } else {
            barangayBox.removeAllItems();
            barangayBox.addItem("SELECT BARANGAY");
            barangayBox.setEnabled(false);
        }
    }
    
    private void searchResidents() {
        String keyword = searchField.getText().trim().toUpperCase();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        
        List<Resident> results = dao.searchByKeyword(keyword);
        updateTableWithResults(results);
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No residents found matching '" + keyword + "'", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void loadTableData() {
        List<Resident> residents = dao.getAllInhabitants();
        updateTableWithResults(residents);
    }
    
    private void updateTableWithResults(List<Resident> residents) {
        tableModel.setRowCount(0);
        for (Resident r : residents) {
            tableModel.addRow(new Object[]{
                r.getLastName(),
                r.getFirstName(),
                r.getMiddleName(),
                r.getAge(),
                r.getSex(),
                r.getCivilStatus(),
                r.getProfessionOccupation(),
                r.getBarangay(),
                r.getResidentVoter(),
                r.getSectoralGroup(),
                r.getPhoneNumber()
            });
        }
    }
}