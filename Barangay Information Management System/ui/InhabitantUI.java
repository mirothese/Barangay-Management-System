// ui/InhabitantUI.java
package ui;

import models.Inhabitant;
import dao.InhabitantDAO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class InhabitantUI extends JFrame {
    private InhabitantDAO dao;
    private JTable residentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchFilter;
    
    // Form fields for adding/editing
    private JDialog formDialog;
    private JTextField nameField, addressField, barangayField, cityField, provinceField;
    private JTextField phoneField, emailField, occupationField, religionField, citizenshipField;
    private JComboBox<String> sexBox, civilStatusBox, sectoralBox;
    private JCheckBox voterCheck;
    private JSpinner daySpinner, monthSpinner, yearSpinner;
    private boolean isEditMode = false;
    private Inhabitant editingInhabitant;
    
    public InhabitantUI(JFrame parent) {
        dao = new InhabitantDAO();
        setTitle("Barangay Inhabitant Profiling System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        
        initUI();
        loadTableData();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Menu Bar
        createMenuBar();
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Search Residents"));
        
        panel.add(new JLabel("Search by:"));
        searchFilter = new JComboBox<>(new String[]{"Name", "Occupation", "Barangay"});
        panel.add(searchFilter);
        
        searchField = new JTextField(20);
        panel.add(searchField);
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchResidents());
        panel.add(searchBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadTableData());
        panel.add(refreshBtn);
        
        JButton clearSearchBtn = new JButton("Clear");
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadTableData();
        });
        panel.add(clearSearchBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resident Records"));
        
        // Table columns
        String[] columns = {"Name", "Age", "Sex", "Civil Status", "Occupation", 
                           "Barangay", "Voter", "Sectoral Group", "Contact"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        residentTable = new JTable(tableModel);
        residentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        residentTable.setRowHeight(25);
        residentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column widths
        residentTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        residentTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        residentTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        residentTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        residentTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        residentTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        residentTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        residentTable.getColumnModel().getColumn(7).setPreferredWidth(120);
        residentTable.getColumnModel().getColumn(8).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(residentTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton addBtn = createButton("Add New Resident", new Color(46, 204, 113));
        JButton editBtn = createButton("Edit Selected", new Color(52, 152, 219));
        JButton deleteBtn = createButton("Delete Selected", new Color(231, 76, 60));
        JButton viewBtn = createButton("View Details", new Color(155, 89, 182));
        JButton statsBtn = createButton("View Statistics", new Color(230, 126, 34));
        
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
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        return button;
    }
    
    private void showAddForm() {
        isEditMode = false;
        editingInhabitant = null;
        createFormDialog("Add New Resident");
    }
    
    private void editSelectedResident() {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            editingInhabitant = dao.findByName(name);
            if (editingInhabitant != null) {
                isEditMode = true;
                createFormDialog("Edit Resident");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedResident() {
        int selectedRow = residentTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete " + name + "?\nThis action cannot be undone.", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteInhabitant(name);
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
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            Inhabitant inhabitant = dao.findByName(name);
            if (inhabitant != null) {
                showViewDialog(inhabitant);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a resident to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void createFormDialog(String title) {
        formDialog = new JDialog(this, title, true);
        formDialog.setSize(600, 700);
        formDialog.setLocationRelativeTo(this);
        formDialog.setLayout(new BorderLayout());
        
        // Create form panel with scrolling
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Personal Information Section
        addSectionHeader(formPanel, "PERSONAL INFORMATION", gbc, row++);
        
        nameField = new JTextField(20);
        addField(formPanel, "Full Name:", nameField, gbc, row++);
        
        // Sex dropdown
        sexBox = new JComboBox<>(new String[]{"MALE", "FEMALE"});
        addField(formPanel, "Sex:", sexBox, gbc, row++);
        
        // Civil Status dropdown
        civilStatusBox = new JComboBox<>(new String[]{"SINGLE", "MARRIED", "WIDOW/ER", "SEPARATED"});
        addField(formPanel, "Civil Status:", civilStatusBox, gbc, row++);
        
        // Date of Birth
        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        yearSpinner = new JSpinner(new SpinnerNumberModel(1990, 1900, 2024, 1));
        monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        dobPanel.add(new JLabel("Year:")); dobPanel.add(yearSpinner);
        dobPanel.add(new JLabel("Month:")); dobPanel.add(monthSpinner);
        dobPanel.add(new JLabel("Day:")); dobPanel.add(daySpinner);
        addField(formPanel, "Date of Birth:", dobPanel, gbc, row++);
        
        // Address Information Section
        addSectionHeader(formPanel, "ADDRESS INFORMATION", gbc, row++);
        
        addressField = new JTextField(20);
        addField(formPanel, "Street Address:", addressField, gbc, row++);
        
        barangayField = new JTextField(20);
        addField(formPanel, "Barangay:", barangayField, gbc, row++);
        
        cityField = new JTextField(20);
        addField(formPanel, "City/Municipality:", cityField, gbc, row++);
        
        provinceField = new JTextField(20);
        addField(formPanel, "Province:", provinceField, gbc, row++);
        
        // Contact Information Section
        addSectionHeader(formPanel, "CONTACT INFORMATION", gbc, row++);
        
        phoneField = new JTextField(20);
        addField(formPanel, "Phone Number:", phoneField, gbc, row++);
        
        emailField = new JTextField(20);
        addField(formPanel, "Email Address:", emailField, gbc, row++);
        
        // Additional Information Section
        addSectionHeader(formPanel, "ADDITIONAL INFORMATION", gbc, row++);
        
        occupationField = new JTextField(20);
        addField(formPanel, "Occupation:", occupationField, gbc, row++);
        
        religionField = new JTextField(20);
        addField(formPanel, "Religion:", religionField, gbc, row++);
        
        citizenshipField = new JTextField("Filipino", 20);
        addField(formPanel, "Citizenship:", citizenshipField, gbc, row++);
        
        sectoralBox = new JComboBox<>(new String[]{"None", "Senior Citizen", "PWD", "OFW", "Solo Parent"});
        addField(formPanel, "Sectoral Group:", sectoralBox, gbc, row++);
        
        voterCheck = new JCheckBox("Yes");
        addField(formPanel, "Registered Voter:", voterCheck, gbc, row++);
        
        // Fill data if editing
        if (isEditMode && editingInhabitant != null) {
            fillFormData();
        }
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        formDialog.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        saveBtn.addActionListener(e -> saveResident());
        cancelBtn.addActionListener(e -> formDialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        formDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        formDialog.setVisible(true);
    }
    
    private void fillFormData() {
        nameField.setText(editingInhabitant.getName());
        sexBox.setSelectedItem(editingInhabitant.getSex());
        civilStatusBox.setSelectedItem(editingInhabitant.getCivilStatus());
        
        if (editingInhabitant.getBirthDate() != null) {
            yearSpinner.setValue(editingInhabitant.getBirthDate().getYear());
            monthSpinner.setValue(editingInhabitant.getBirthDate().getMonthValue());
            daySpinner.setValue(editingInhabitant.getBirthDate().getDayOfMonth());
        }
        
        addressField.setText(editingInhabitant.getAddress());
        barangayField.setText(editingInhabitant.getBarangay());
        cityField.setText(editingInhabitant.getCityMunicipality());
        provinceField.setText(editingInhabitant.getProvince());
        phoneField.setText(editingInhabitant.getPhoneNumber());
        emailField.setText(editingInhabitant.getEmail());
        occupationField.setText(editingInhabitant.getOccupation());
        religionField.setText(editingInhabitant.getReligion());
        citizenshipField.setText(editingInhabitant.getCitizenship());
        sectoralBox.setSelectedItem(editingInhabitant.getSectoralGroup());
        voterCheck.setSelected(editingInhabitant.isVoter());
    }
    
    private void saveResident() {
        try {
            Inhabitant inhabitant = new Inhabitant();
            
            // Set values from form
            inhabitant.setName(nameField.getText());
            inhabitant.setSex((String) sexBox.getSelectedItem());
            inhabitant.setCivilStatus((String) civilStatusBox.getSelectedItem());
            
            int year = (Integer) yearSpinner.getValue();
            int month = (Integer) monthSpinner.getValue();
            int day = (Integer) daySpinner.getValue();
            inhabitant.setBirthDate(LocalDate.of(year, month, day));
            
            inhabitant.setAddress(addressField.getText());
            inhabitant.setBarangay(barangayField.getText());
            inhabitant.setCityMunicipality(cityField.getText());
            inhabitant.setProvince(provinceField.getText());
            inhabitant.setPhoneNumber(phoneField.getText());
            inhabitant.setEmail(emailField.getText());
            inhabitant.setOccupation(occupationField.getText());
            inhabitant.setReligion(religionField.getText());
            inhabitant.setCitizenship(citizenshipField.getText());
            inhabitant.setSectoralGroup((String) sectoralBox.getSelectedItem());
            inhabitant.setVoter(voterCheck.isSelected());
            
            // Validate required fields
            if (inhabitant.getName().isEmpty()) {
                JOptionPane.showMessageDialog(formDialog, "Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isEditMode && editingInhabitant != null) {
                dao.updateInhabitant(inhabitant);
                JOptionPane.showMessageDialog(formDialog, "Resident updated successfully!");
            } else {
                dao.addInhabitant(inhabitant);
                JOptionPane.showMessageDialog(formDialog, "Resident added successfully!");
            }
            
            formDialog.dispose();
            loadTableData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(formDialog, "Error saving resident: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showViewDialog(Inhabitant inhabitant) {
        JDialog viewDialog = new JDialog(this, "Resident Details", true);
        viewDialog.setSize(500, 600);
        viewDialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder details = new StringBuilder();
        details.append("=".repeat(50)).append("\n");
        details.append("RESIDENT INFORMATION\n");
        details.append("=".repeat(50)).append("\n\n");
        
        details.append("PERSONAL INFORMATION:\n");
        details.append("  Name: ").append(inhabitant.getName()).append("\n");
        details.append("  Age: ").append(inhabitant.getAge()).append("\n");
        details.append("  Sex: ").append(inhabitant.getSex()).append("\n");
        details.append("  Civil Status: ").append(inhabitant.getCivilStatus()).append("\n");
        details.append("  Birth Date: ").append(inhabitant.getBirthDate()).append("\n\n");
        
        details.append("ADDRESS:\n");
        details.append("  ").append(inhabitant.getFullAddress()).append("\n\n");
        
        details.append("CONTACT:\n");
        details.append("  Phone: ").append(inhabitant.getPhoneNumber()).append("\n");
        details.append("  Email: ").append(inhabitant.getEmail()).append("\n\n");
        
        details.append("ADDITIONAL INFO:\n");
        details.append("  Occupation: ").append(inhabitant.getOccupation()).append("\n");
        details.append("  Religion: ").append(inhabitant.getReligion()).append("\n");
        details.append("  Citizenship: ").append(inhabitant.getCitizenship()).append("\n");
        details.append("  Sectoral Group: ").append(inhabitant.getSectoralGroup()).append("\n");
        details.append("  Registered Voter: ").append(inhabitant.isVoter() ? "Yes" : "No").append("\n");
        
        textArea.setText(details.toString());
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        viewDialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> viewDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        viewDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        viewDialog.setVisible(true);
    }
    
    private void showStatistics() {
        JDialog statDialog = new JDialog(this, "Resident Statistics", true);
        statDialog.setSize(500, 400);
        statDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int total = dao.getTotalResidents();
        int male = dao.getMaleCount();
        int female = dao.getFemaleCount();
        int voters = dao.getVoterCount();
        int seniors = dao.getSeniorCitizenCount();
        int pwd = dao.getCountBySector("PWD");
        int ofw = dao.getCountBySector("OFW");
        int soloParent = dao.getCountBySector("Solo Parent");
        
        addStatRow(panel, "Total Residents:", String.valueOf(total), gbc, 0);
        addStatRow(panel, "Male:", String.valueOf(male), gbc, 1);
        addStatRow(panel, "Female:", String.valueOf(female), gbc, 2);
        addStatRow(panel, "Registered Voters:", String.valueOf(voters), gbc, 3);
        addStatRow(panel, "Senior Citizens:", String.valueOf(seniors), gbc, 4);
        addStatRow(panel, "PWD:", String.valueOf(pwd), gbc, 5);
        addStatRow(panel, "OFW:", String.valueOf(ofw), gbc, 6);
        addStatRow(panel, "Solo Parents:", String.valueOf(soloParent), gbc, 7);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> statDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        
        statDialog.add(panel, BorderLayout.CENTER);
        statDialog.add(buttonPanel, BorderLayout.SOUTH);
        statDialog.setVisible(true);
    }
    
    private void addStatRow(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setForeground(new Color(41, 128, 185));
        panel.add(valueLabel, gbc);
    }
    
    private void addSectionHeader(JPanel panel, String title, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel header = new JLabel(title);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(new Color(41, 128, 185));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        panel.add(header, gbc);
        gbc.gridwidth = 1;
    }
    
    private void addField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private void searchResidents() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        
        List<Inhabitant> results = dao.searchByKeyword(keyword);
        updateTableWithResults(results);
        
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No residents found matching '" + keyword + "'", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void loadTableData() {
        List<Inhabitant> inhabitants = dao.getAllInhabitants();
        updateTableWithResults(inhabitants);
    }
    
    private void updateTableWithResults(List<Inhabitant> inhabitants) {
        tableModel.setRowCount(0);
        for (Inhabitant i : inhabitants) {
            tableModel.addRow(new Object[]{
                i.getName(),
                i.getAge(),
                i.getSex(),
                i.getCivilStatus(),
                i.getOccupation(),
                i.getBarangay(),
                i.isVoter() ? "Yes" : "No",
                i.getSectoralGroup(),
                i.getPhoneNumber()
            });
        }
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> dispose());
        fileMenu.add(closeItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpItem = new JMenuItem("User Guide");
        helpItem.addActionListener(e -> showHelp());
        helpMenu.add(helpItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void showHelp() {
        String helpText = "BARANGAY INHABITANT PROFILING SYSTEM\n\n" +
                         "Features:\n" +
                         "• Add New Resident - Fill out the form with resident information\n" +
                         "• Edit Resident - Select a resident and click Edit\n" +
                         "• Delete Resident - Select a resident and click Delete\n" +
                         "• View Details - See complete resident information\n" +
                         "• Search - Find residents by name, occupation, or barangay\n" +
                         "• Statistics - View demographic statistics\n\n" +
                         "All data is automatically saved to the data folder.";
        
        JOptionPane.showMessageDialog(this, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}