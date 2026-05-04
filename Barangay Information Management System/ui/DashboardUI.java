// ui/DashboardUI.java
package ui;

import dao.DashboardDAO;
import models.DashboardStats;
import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JFrame {
    private DashboardDAO dashboardDAO;
    private DashboardStats stats;
    
    // UI Components
    private JLabel totalResidentsLabel, totalOfficialsLabel;
    private JLabel maleLabel, femaleLabel;
    private JLabel voterLabel, seniorLabel;
    private JProgressBar maleProgress, femaleProgress;
    private JPanel statsPanel;
    private Timer refreshTimer;
    
    public DashboardUI() {
        dashboardDAO = new DashboardDAO();
        setTitle("Barangay Management System - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        loadStatistics();
        initUI();
        startAutoRefresh();
    }
    
    private void loadStatistics() {
        stats = dashboardDAO.getDashboardStats();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Statistics Cards
        mainPanel.add(createResidentCard());
        mainPanel.add(createOfficialCard());
        mainPanel.add(createDemographicsCard());
        mainPanel.add(createQuickAccessCard());
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        // Menu Bar
        createMenuBar();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Barangay Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Dashboard Overview", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createResidentCard() {
        JPanel card = createCard("Residents Overview", new Color(52, 152, 219));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        totalResidentsLabel = createValueLabel(String.valueOf(stats.getTotalResidents()));
        
        addStatRow(card, "Total Residents:", totalResidentsLabel, gbc, 0);
        addStatRow(card, "Registered Voters:", createValueLabel(String.valueOf(stats.getVoterCount())), gbc, 1);
        addStatRow(card, "Senior Citizens:", createValueLabel(String.valueOf(stats.getSeniorCitizenCount())), gbc, 2);
        
        return card;
    }
    
    private JPanel createOfficialCard() {
        JPanel card = createCard("Officials Overview", new Color(46, 204, 113));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        totalOfficialsLabel = createValueLabel(String.valueOf(stats.getTotalOfficials()));
        
        addStatRow(card, "Total Officials:", totalOfficialsLabel, gbc, 0);
        addStatRow(card, "Active Officials:", createValueLabel(String.valueOf(stats.getActiveOfficials())), gbc, 1);
        
        JButton viewOfficialsBtn = new JButton("Manage Officials");
        viewOfficialsBtn.addActionListener(e -> openOfficialsModule());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        card.add(viewOfficialsBtn, gbc);
        
        return card;
    }
    
    private JPanel createDemographicsCard() {
        JPanel card = createCard("Demographics", new Color(155, 89, 182));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        maleLabel = createValueLabel(String.valueOf(stats.getMaleCount()));
        femaleLabel = createValueLabel(String.valueOf(stats.getFemaleCount()));
        
        addStatRow(card, "Male:", maleLabel, gbc, 0);
        addStatRow(card, "Female:", femaleLabel, gbc, 1);
        
        // Progress bars
        int malePercent = (int) stats.getMalePercentage();
        int femalePercent = (int) stats.getFemalePercentage();
        
        maleProgress = new JProgressBar(0, 100);
        maleProgress.setValue(malePercent);
        maleProgress.setStringPainted(true);
        maleProgress.setString(malePercent + "% Male");
        maleProgress.setForeground(new Color(52, 152, 219));
        
        femaleProgress = new JProgressBar(0, 100);
        femaleProgress.setValue(femalePercent);
        femaleProgress.setStringPainted(true);
        femaleProgress.setString(femalePercent + "% Female");
        femaleProgress.setForeground(new Color(231, 76, 60));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        card.add(maleProgress, gbc);
        
        gbc.gridy = 3;
        card.add(femaleProgress, gbc);
        
        return card;
    }
    
    private JPanel createQuickAccessCard() {
        JPanel card = createCard("Quick Access", new Color(230, 126, 34));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton residentsBtn = new JButton("Manage Residents");
        JButton officialsBtn = new JButton("Manage Officials");
        JButton refreshBtn = new JButton("Refresh Data");
        
        residentsBtn.setBackground(new Color(52, 152, 219));
        residentsBtn.setForeground(Color.WHITE);
        officialsBtn.setBackground(new Color(46, 204, 113));
        officialsBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(new Color(155, 89, 182));
        refreshBtn.setForeground(Color.WHITE);
        
        residentsBtn.addActionListener(e -> openInhabitantModule());
        officialsBtn.addActionListener(e -> openOfficialsModule());
        refreshBtn.addActionListener(e -> refreshDashboard());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(residentsBtn, gbc);
        
        gbc.gridy = 1;
        card.add(officialsBtn, gbc);
        
        gbc.gridy = 2;
        card.add(refreshBtn, gbc);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel statusLabel = new JLabel("System Ready | Last Updated: " + new java.util.Date());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(statusLabel);
        
        return panel;
    }
    
    private JPanel createCard(String title, Color color) {
        JPanel card = new JPanel();
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(titleLabel, BorderLayout.NORTH);
        
        return card;
    }
    
    private JLabel createValueLabel(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(41, 128, 185));
        return label;
    }
    
    private void addStatRow(JPanel panel, String label, JLabel value, GridBagConstraints gbc, int row) {
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(textLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(value, gbc);
        gbc.anchor = GridBagConstraints.WEST;
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        JMenu modulesMenu = new JMenu("Modules");
        JMenuItem inhabitantItem = new JMenuItem("Barangay Inhabitant Profiling");
        JMenuItem officialsItem = new JMenuItem("Barangay Officials Information");
        
        inhabitantItem.addActionListener(e -> openInhabitantModule());
        officialsItem.addActionListener(e -> openOfficialsModule());
        
        modulesMenu.add(inhabitantItem);
        modulesMenu.add(officialsItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(modulesMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void refreshDashboard() {
        loadStatistics();
        
        totalResidentsLabel.setText(String.valueOf(stats.getTotalResidents()));
        totalOfficialsLabel.setText(String.valueOf(stats.getTotalOfficials()));
        maleLabel.setText(String.valueOf(stats.getMaleCount()));
        femaleLabel.setText(String.valueOf(stats.getFemaleCount()));
        voterLabel.setText(String.valueOf(stats.getVoterCount()));
        seniorLabel.setText(String.valueOf(stats.getSeniorCitizenCount()));
        
        int malePercent = (int) stats.getMalePercentage();
        int femalePercent = (int) stats.getFemalePercentage();
        
        maleProgress.setValue(malePercent);
        maleProgress.setString(malePercent + "% Male");
        femaleProgress.setValue(femalePercent);
        femaleProgress.setString(femalePercent + "% Female");
        
        JOptionPane.showMessageDialog(this, "Dashboard refreshed!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void startAutoRefresh() {
        refreshTimer = new Timer(300000, e -> refreshDashboard()); // Refresh every 5 minutes
        refreshTimer.start();
    }
    
    private void openInhabitantModule() {
        SwingUtilities.invokeLater(() -> {
            new InhabitantUI(this).setVisible(true);
        });
    }
    
    private void openOfficialsModule() {
        SwingUtilities.invokeLater(() -> {
            new OfficialUI(this).setVisible(true);
        });
    }
    
    private void showAbout() {
        String message = "Barangay Management System v1.0\n\n" +
                         "Modules:\n" +
                         "• Barangay Inhabitant Profiling System\n" +
                         "• Barangay Officials Information System\n" +
                         "• Dashboard & Statistics\n\n" +
                         "Developed for Barangay Management\n" +
                         "© 2024";
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}