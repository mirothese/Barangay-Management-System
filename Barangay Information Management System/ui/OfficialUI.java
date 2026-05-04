// ui/OfficialUI.java
package ui;

import models.Official;
import dao.OfficialDAO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class OfficialUI extends JFrame {
    private OfficialDAO dao;
    private JTable officialTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    // Form fields
    private JDialog formDialog;
    private JTextField nameField, positionField, committeeField, contactField;
    private JComboBox<String> sexBox;
    private JCheckBox activeCheck;
    private JSpinner birthYearSpinner, birthMonthSpinner, birthDaySpinner;
    private JSpinner termStartYear, termStartMonth, termStartDay;
    private JSpinner termEndYear, termEndMonth, termEndDay;
    private boolean isEditMode = false;
    private Official editingOfficial;
    
    public OfficialUI(JFrame parent) {
        dao = new OfficialDAO();
        setTitle("Barangay Officials Information System");
        setSize(1100, 600);
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
        panel.setBorder(BorderFactory.createTitledBorder("Search Officials"));
        
        panel.add(new JLabel("Name:"));
        searchField = new JTextField(20);
        panel.add(searchField);
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchOfficials());
        panel.add(searchBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadTableData());
        panel.add(refreshBtn);
        
        JButton showActiveBtn = new JButton("Show Active Only");
        showActiveBtn.addActionListener(e -> showActiveOfficials());
        panel.add(showActiveBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Barangay Officials"));
        
        String[] columns = {"Name", "Position", "Sex", "Age", "Term Start", "Term End", "Committee", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        officialTable = new JTable(tableModel);
        officialTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        officialTable.setRowHeight(25);
        officialTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column widths
        officialTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        officialTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        officialTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        officialTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        officialTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        officialTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        officialTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        officialTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(officialTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton addBtn = createButton("Add Official", new Color(46, 204, 113));
        JButton editBtn = createButton("Edit Selected", new Color(52, 152, 219));
        JButton deleteBtn = createButton("Delete Selected", new Color(231, 76, 60));
        JButton viewBtn = createButton("View Details", new Color(155, 89, 182));
        
        addBtn.addActionListener(e -> showAddForm());
        editBtn.addActionListener(e -> editSelectedOfficial());
        deleteBtn.addActionListener(e -> deleteSelectedOfficial());
        viewBtn.addActionListener(e -> viewSelectedOfficial());
        
        panel.add(addBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
        panel.add(viewBtn);
        
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
        editingOfficial = null;
        createFormDialog("Add New Barangay Official");
    }
    
    private void editSelectedOfficial() {
        int selectedRow = officialTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            editingOfficial = dao.findByName(name);
            if (editingOfficial != null) {
                isEditMode = true;
                createFormDialog("Edit Barangay Official");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an official to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedOfficial() {
        int selectedRow = officialTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete " + name + "?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dao.deleteOfficial(name);
                loadTableData();
                JOptionPane.showMessageDialog(this, "Official deleted successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an official to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void viewSelectedOfficial() {
        int selectedRow = officialTable.getSelectedRow();
        if (selectedRow >= 0) {
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            Official official = dao.findByName(name);
            if (official != null) {
                showViewDialog(official);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an official to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void createFormDialog(String title) {
        formDialog = new JDialog(this, title, true);
        formDialog.setSize(550, 600);
        formDialog.setLocationRelativeTo(this);
        formDialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Basic Information
        addSectionHeader(formPanel, "BASIC INFORMATION", gbc, row++);
        
        nameField = new JTextField(20);
        addField(formPanel, "Full Name:", nameField, gbc, row++);
        
        positionField = new JTextField(20);
        addField(formPanel, "Position:", positionField, gbc, row++);
        
        sexBox = new JComboBox<>(new String[]{"MALE", "FEMALE"});
        addField(formPanel, "Sex:", sexBox, gbc, row++);
        
        // Birth Date
        JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        birthYearSpinner = new JSpinner(new SpinnerNumberModel(1975, 1950, 2005, 1));
        birthMonthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        birthDaySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        birthPanel.add(new JLabel("Year:")); birthPanel.add(birthYearSpinner);
        birthPanel.add(new JLabel("Month:")); birthPanel.add(birthMonthSpinner);
        birthPanel.add(new JLabel("Day:")); birthPanel.add(birthDaySpinner);
        addField(formPanel, "Birth Date:", birthPanel, gbc, row++);
        
        // Term Information
        addSectionHeader(formPanel, "TERM INFORMATION", gbc, row++);
        
        JPanel termStartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        termStartYear = new JSpinner(new SpinnerNumberModel(2023, 2020, 2030, 1));
        termStartMonth = new JSpinner(new SpinnerNumberModel(7, 1, 12, 1));
        termStartDay = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        termStartPanel.add(new JLabel("Year:")); termStartPanel.add(termStartYear);
        termStartPanel.add(new JLabel("Month:")); termStartPanel.add(termStartMonth);
        termStartPanel.add(new JLabel("Day:")); termStartPanel.add(termStartDay);
        addField(formPanel, "Term Start:", termStartPanel, gbc, row++);
        
        JPanel termEndPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        termEndYear = new JSpinner(new SpinnerNumberModel(2026, 2020, 2030, 1));
        termEndMonth = new JSpinner(new SpinnerNumberModel(6, 1, 12, 1));
        termEndDay = new JSpinner(new SpinnerNumberModel(30, 1, 31, 1));
        termEndPanel.add(new JLabel("Year:")); termEndPanel.add(termEndYear);
        termEndPanel.add(new JLabel("Month:")); termEndPanel.add(termEndMonth);
        termEndPanel.add(new JLabel("Day:")); termEndPanel.add(termEndDay);
        addField(formPanel, "Term End:", termEndPanel, gbc, row++);
        
        // Additional Information
        addSectionHeader(formPanel, "ADDITIONAL INFORMATION", gbc, row++);
        
        committeeField = new JTextField(20);
        addField(formPanel, "Committee:", committeeField, gbc, row++);
        
        contactField = new JTextField(20);
        addField(formPanel, "Contact Number:", contactField, gbc, row++);
        
        activeCheck = new JCheckBox("Currently Active");
        activeCheck.setSelected(true);
        addField(formPanel, "Status:", activeCheck, gbc, row++);
        
        // Fill data if editing
        if (isEditMode && editingOfficial != null) {
            fillFormData();
        }
        
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
        
        saveBtn.addActionListener(e -> saveOfficial());
        cancelBtn.addActionListener(e -> formDialog.dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        formDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        formDialog.setVisible(true);
    }
    
    private void fillFormData() {
        nameField.setText(editingOfficial.getName());
        positionField.setText(editingOfficial.getPosition());
        sexBox.setSelectedItem(editingOfficial.getSex());
        
        if (editingOfficial.getBirthDate() != null) {
            birthYearSpinner.setValue(editingOfficial.getBirthDate().getYear());
            birthMonthSpinner.setValue(editingOfficial.getBirthDate().getMonthValue());
            birthDaySpinner.setValue(editingOfficial.getBirthDate().getDayOfMonth());
        }
        
        if (editingOfficial.getTermStart() != null) {
            termStartYear.setValue(editingOfficial.getTermStart().getYear());
            termStartMonth.setValue(editingOfficial.getTermStart().getMonthValue());
            termStartDay.setValue(editingOfficial.getTermStart().getDayOfMonth());
        }
        
        if (editingOfficial.getTermEnd() != null) {
            termEndYear.setValue(editingOfficial.getTermEnd().getYear());
            termEndMonth.setValue(editingOfficial.getTermEnd().getMonthValue());
            termEndDay.setValue(editingOfficial.getTermEnd().getDayOfMonth());
        }
        
        committeeField.setText(editingOfficial.getCommittee());
        contactField.setText(editingOfficial.getContactNumber());
        activeCheck.setSelected(editingOfficial.isActive());
    }
    
    private void saveOfficial() {
        try {
            Official official = new Official();
            
            official.setName(nameField.getText());
            official.setPosition(positionField.getText());
            official.setSex((String) sexBox.getSelectedItem());
            
            int birthYear = (Integer) birthYearSpinner.getValue();
            int birthMonth = (Integer) birthMonthSpinner.getValue();
            int birthDay = (Integer) birthDaySpinner.getValue();
            official.setBirthDate(LocalDate.of(birthYear, birthMonth, birthDay));
            
            int startYear = (Integer) termStartYear.getValue();
            int startMonth = (Integer) termStartMonth.getValue();
            int startDay = (Integer) termStartDay.getValue();
            official.setTermStart(LocalDate.of(startYear, startMonth, startDay));
            
            int endYear = (Integer) termEndYear.getValue();
            int endMonth = (Integer) termEndMonth.getValue();
            int endDay = (Integer) termEndDay.getValue();
            official.setTermEnd(LocalDate.of(endYear, endMonth, endDay));
            
            official.setCommittee(committeeField.getText());
            official.setContactNumber(contactField.getText());
            official.setActive(activeCheck.isSelected());
            
            // Validate required fields
            if (official.getName().isEmpty() || official.getPosition().isEmpty()) {
                JOptionPane.showMessageDialog(formDialog, "Name and Position are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (isEditMode && editingOfficial != null) {
                dao.updateOfficial(official);
                JOptionPane.showMessageDialog(formDialog, "Official updated successfully!");
            } else {
                dao.addOfficial(official);
                JOptionPane.showMessageDialog(formDialog, "Official added successfully!");
            }
            
            formDialog.dispose();
            loadTableData();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(formDialog, "Error saving official: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showViewDialog(Official official) {
        JDialog viewDialog = new JDialog(this, "Official Details", true);
        viewDialog.setSize(450, 500);
        viewDialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder details = new StringBuilder();
        details.append("=".repeat(45)).append("\n");
        details.append("BARANGAY OFFICIAL INFORMATION\n");
        details.append("=".repeat(45)).append("\n\n");
        
        details.append("Name: ").append(official.getName()).append("\n");
        details.append("Position: ").append(official.getPosition()).append("\n");
        details.append("Sex: ").append(official.getSex()).append("\n");
        details.append("Age: ").append(official.getAge()).append("\n");
        details.append("Birth Date: ").append(official.getBirthDate()).append("\n\n");
        
        details.append("Term Start: ").append(official.getTermStart()).append("\n");
        details.append("Term End: ").append(official.getTermEnd()).append("\n");
        details.append("Status: ").append(official.isCurrentlyInOffice() ? "Currently in Office" : "Not in Office").append("\n\n");
        
        details.append("Committee: ").append(official.getCommittee()).append("\n");
        details.append("Contact: ").append(official.getContactNumber()).append("\n");
        details.append("Active: ").append(official.isActive() ? "Yes" : "No");
        
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
    
    private void searchOfficials() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        
        List<Official> results = dao.getAllOfficials();
        tableModel.setRowCount(0);
        
        for (Official o : results) {
            if (o.getName().toLowerCase().contains(keyword)) {
                addOfficialToTable(o);
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No officials found matching '" + keyword + "'");
        }
    }
    
    private void showActiveOfficials() {
        List<Official> activeOfficials = dao.getActiveOfficials();
        tableModel.setRowCount(0);
        for (Official o : activeOfficials) {
            addOfficialToTable(o);
        }
        
        if (activeOfficials.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active officials found.");
        }
    }
    
    private void loadTableData() {
        List<Official> officials = dao.getAllOfficials();
        tableModel.setRowCount(0);
        for (Official o : officials) {
            addOfficialToTable(o);
        }
    }
    
    private void addOfficialToTable(Official o) {
        String status = o.isCurrentlyInOffice() ? "Active" : "Inactive";
        tableModel.addRow(new Object[]{
            o.getName(),
            o.getPosition(),
            o.getSex(),
            o.getAge(),
            o.getTermStart(),
            o.getTermEnd(),
            o.getCommittee(),
            status
        });
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
        String helpText = "BARANGAY OFFICIALS INFORMATION SYSTEM\n\n" +
                         "Features:\n" +
                         "• Add New Official - Fill out the form with official information\n" +
                         "• Edit Official - Select an official and click Edit\n" +
                         "• Delete Official - Select an official and click Delete\n" +
                         "• View Details - See complete official information\n" +
                         "• Search - Find officials by name\n" +
                         "• Show Active Only - Display currently serving officials\n\n" +
                         "Term dates determine if an official is currently in office.";
        
        JOptionPane.showMessageDialog(this, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}