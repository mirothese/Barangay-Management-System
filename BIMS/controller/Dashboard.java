package controller;

import dao.ResidentDAO;
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JPanel {
    
    private ResidentDAO dao;
    
    private JLabel totalResidentsLabel, totalNonResidentsLabel, totalVotersLabel, totalDeceasedLabel;
    private JPanel statsPanel;
    private JPanel chartsPanel;
    
    private final Color BG_COLOR = Color.WHITE;
    private final Color CARD_BG = new Color(248, 249, 250);
    private final Color ACCENT_BLUE = new Color(13, 110, 253);
    private final Color ACCENT_GREEN = new Color(25, 135, 84);
    private final Color ACCENT_RED = new Color(220, 53, 69);
    private final Color ACCENT_PURPLE = new Color(111, 66, 193);
    
    public Dashboard() {
        dao = new ResidentDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initUI();
        refreshData();
    }
    
    private void initUI() {
        // Create a wrapper panel that holds everything
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);
        
        // 1. HEADER SECTION
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel titleLabel = new JLabel("BARANGAY DASHBOARD OVERVIEW", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_BLUE);
        
        // Refresh Button
        JButton refreshButton = new JButton("🔄 REFRESH DATA");
        refreshButton.setBackground(ACCENT_GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(new Color(20, 120, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButton.setBackground(ACCENT_GREEN);
            }
        });
        
        // Click action
        refreshButton.addActionListener(e -> {
            refreshButton.setText("⏳ UPDATING...");
            refreshButton.setEnabled(false);
            
            // Use Timer for brief delay
            Timer timer = new Timer(500, event -> {
                refreshData();
                refreshButton.setText("🔄 REFRESH DATA");
                refreshButton.setEnabled(true);
                
                JOptionPane.showMessageDialog(Dashboard.this, 
                    "Dashboard updated successfully!", 
                    "Update Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
            timer.setRepeats(false);
            timer.start();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.add(refreshButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // 2. STATS CARDS SECTION
        statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(BG_COLOR);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        totalResidentsLabel = addStatsCard("TOTAL RESIDENTS", "0", ACCENT_BLUE, "\uD83D\uDC65");
        totalNonResidentsLabel = addStatsCard("NON-RESIDENTS", "0", ACCENT_PURPLE, "\uD83C\uDF0D");
        totalVotersLabel = addStatsCard("REGISTERED VOTERS", "0", ACCENT_GREEN, "\uD83D\uDDF3\uFE0F");
        totalDeceasedLabel = addStatsCard("DECEASED", "0", ACCENT_RED, "\uD83D\uDC80");
        
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // 3. CHARTS SECTION
        chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(BG_COLOR);
        
        JPanel genderChartPanel = createGenderChartPanel();
        chartsPanel.add(genderChartPanel);
        
        JPanel ageChartPanel = createAgeChartPanel();
        chartsPanel.add(ageChartPanel);
        
        contentPanel.add(chartsPanel);
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createGenderChartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("GENDER DISTRIBUTION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        GenderPieChart pieChart = new GenderPieChart();
        pieChart.setPreferredSize(new Dimension(300, 300));
        panel.add(pieChart, BorderLayout.CENTER);
        
        panel.putClientProperty("pieChart", pieChart);
        return panel;
    }
    
    private JPanel createAgeChartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("AGE DISTRIBUTION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(33, 37, 41));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        AgeBarChart barChart = new AgeBarChart();
        barChart.setPreferredSize(new Dimension(300, 300));
        panel.add(barChart, BorderLayout.CENTER);
        
        panel.putClientProperty("barChart", barChart);
        return panel;
    }
    
    private JLabel addStatsCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(108, 117, 125));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(color);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        topPanel.setBackground(CARD_BG);
        topPanel.add(iconLabel);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);
        
        statsPanel.add(card);
        return valueLabel;
    }
    
    public void refreshData() {
        System.out.println("=== REFRESHING DASHBOARD ===");

        dao.reloadFromDisk();
        
        int total = dao.getTotalResidents();
        int nonResidents = dao.getNonResidentCount();
        int voters = dao.getVoterCount();
        int deceased = dao.getDeceasedCount();
        int male = dao.getMaleCount();
        int female = dao.getFemaleCount();
        int children = dao.getChildrenCount();
        int youth = dao.getYouthCount();
        int adult = dao.getAdultCount();
        int seniors = dao.getSeniorCitizenCount();
        
        totalResidentsLabel.setText(String.valueOf(total));
        totalNonResidentsLabel.setText(String.valueOf(nonResidents));
        totalVotersLabel.setText(String.valueOf(voters));
        totalDeceasedLabel.setText(String.valueOf(deceased));
        
        for (Component comp : chartsPanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getClientProperty("pieChart") instanceof GenderPieChart) {
                    GenderPieChart pieChart = (GenderPieChart) panel.getClientProperty("pieChart");
                    pieChart.updateData(male, female);
                }
                if (panel.getClientProperty("barChart") instanceof AgeBarChart) {
                    AgeBarChart barChart = (AgeBarChart) panel.getClientProperty("barChart");
                    barChart.updateData(children, youth, adult, seniors);
                }
            }
        }
        
        statsPanel.revalidate();
        statsPanel.repaint();
        chartsPanel.revalidate();
        chartsPanel.repaint();
        
        System.out.println("=== DASHBOARD UPDATED ===");
    }
    
    private class GenderPieChart extends JPanel {
        private int maleCount = 0;
        private int femaleCount = 0;
        
        public GenderPieChart() {
            setBackground(CARD_BG);
        }
        
        public void updateData(int male, int female) {
            this.maleCount = male;
            this.femaleCount = female;
            repaint();
            revalidate();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int total = maleCount + femaleCount;
            if (total == 0) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                String noData = "No data available";
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(noData)) / 2;
                int y = getHeight() / 2;
                g2.drawString(noData, x, y);
                g2.dispose();
                return;
            }
            
            int size = Math.min(getWidth(), getHeight()) - 60;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            
            double maleAngle = 360.0 * maleCount / total;
            g2.setColor(ACCENT_BLUE);
            g2.fillArc(x, y, size, size, 0, (int) maleAngle);
            
            g2.setColor(ACCENT_RED);
            g2.fillArc(x, y, size, size, (int) maleAngle, (int) (360 - maleAngle));
            
            int legendX = getWidth() - 100;
            int legendY = 30;
            g2.setColor(ACCENT_BLUE);
            g2.fillRect(legendX, legendY, 15, 15);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Male: " + maleCount + " (" + String.format("%.1f", maleCount * 100.0 / total) + "%)", legendX + 20, legendY + 12);
            
            g2.setColor(ACCENT_RED);
            g2.fillRect(legendX, legendY + 25, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawString("Female: " + femaleCount + " (" + String.format("%.1f", femaleCount * 100.0 / total) + "%)", legendX + 20, legendY + 37);
            
            g2.dispose();
        }
    }
    
    private class AgeBarChart extends JPanel {
        private int children = 0, youth = 0, adult = 0, seniors = 0;
        private final String[] AGE_GROUPS = {"0-14", "15-30", "31-59", "60+"};
        private final Color[] BAR_COLORS = {new Color(13, 202, 240), new Color(255, 193, 7), ACCENT_GREEN, ACCENT_RED};
        
        public AgeBarChart() {
            setBackground(CARD_BG);
        }
        
        public void updateData(int children, int youth, int adult, int seniors) {
            this.children = children;
            this.youth = youth;
            this.adult = adult;
            this.seniors = seniors;
            repaint();
            revalidate();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int[] values = {children, youth, adult, seniors};
            int maxValue = 1;
            for (int v : values) if (v > maxValue) maxValue = v;
            if (maxValue == 0) maxValue = 1;
            
            int chartWidth = getWidth() - 100;
            int chartHeight = getHeight() - 80;
            int barWidth = (chartWidth / AGE_GROUPS.length) - 10;
            int startX = 50;
            int startY = getHeight() - 50;
            
            g2.setColor(Color.GRAY);
            g2.drawLine(startX - 10, startY, startX + chartWidth, startY);
            g2.drawLine(startX - 10, startY - chartHeight, startX - 10, startY);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= 4; i++) {
                int y = startY - (i * chartHeight / 4);
                int value = (maxValue * i / 4);
                g2.drawString(String.valueOf(value), startX - 30, y + 3);
            }
            
            for (int i = 0; i < AGE_GROUPS.length; i++) {
                int barHeight = (int) ((double) values[i] / maxValue * chartHeight);
                if (barHeight < 5 && values[i] > 0) barHeight = 5;
                int x = startX + i * (barWidth + 10);
                int y = startY - barHeight;
                
                g2.setColor(BAR_COLORS[i]);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                g2.drawString(String.valueOf(values[i]), x + barWidth / 2 - 10, y - 5);
                
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                g2.drawString(AGE_GROUPS[i], x + barWidth / 2 - 15, startY + 15);
            }
            g2.dispose();
        }
    }
}