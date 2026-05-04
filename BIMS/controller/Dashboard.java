// controller/Dashboard.java
package controller;

import dao.ResidentDAO;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Dashboard extends JPanel {
    
    private ResidentDAO dao;
    
    // UI Components
    private JLabel totalCountLabel, maleCountLabel, femaleCountLabel, voterCountLabel;
    private JPanel statsPanel;
    private JPanel chartsPanel;
    
    // Colors
    private final Color BG_COLOR = Color.WHITE;
    private final Color CARD_BG = new Color(248, 249, 250);
    private final Color ACCENT_BLUE = new Color(13, 110, 253);
    private final Color ACCENT_GREEN = new Color(25, 135, 84);
    private final Color ACCENT_PURPLE = new Color(111, 66, 193);
    private final Color ACCENT_ORANGE = new Color(253, 126, 20);
    private final Color ACCENT_RED = new Color(220, 53, 69);
    private final Color ACCENT_TEAL = new Color(13, 202, 240);
    private final Color ACCENT_YELLOW = new Color(255, 193, 7);
    
    // Chart colors
    private final Color[] GENDER_COLORS = {ACCENT_BLUE, ACCENT_RED};
    private final Color[] AGE_COLORS = {ACCENT_TEAL, ACCENT_YELLOW, ACCENT_GREEN, ACCENT_ORANGE, ACCENT_RED};
    
    public Dashboard() {
        dao = new ResidentDAO();
        setLayout(new BorderLayout(15, 15));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initUI();
        refreshData();
    }
    
    private void initUI() {
        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Stats Cards (Top row)
        statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setOpaque(false);
        
        totalCountLabel = addStatsCard("TOTAL RESIDENTS", "0", ACCENT_BLUE);
        maleCountLabel = addStatsCard("MALE", "0", ACCENT_GREEN);
        femaleCountLabel = addStatsCard("FEMALE", "0", ACCENT_PURPLE);
        voterCountLabel = addStatsCard("REGISTERED VOTERS", "0", ACCENT_ORANGE);
        
        add(statsPanel, BorderLayout.NORTH);
        
        // Charts Panel (Bottom row - 2 columns)
        chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setOpaque(false);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Gender Distribution Chart (Pie Chart)
        JPanel genderChartPanel = createGenderChartPanel();
        chartsPanel.add(genderChartPanel);
        
        // Age Distribution Chart (Bar Chart)
        JPanel ageChartPanel = createAgeChartPanel();
        chartsPanel.add(ageChartPanel);
        
        add(chartsPanel, BorderLayout.CENTER);
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
        
        // Custom Pie Chart Panel
        GenderPieChart pieChart = new GenderPieChart();
        pieChart.setPreferredSize(new Dimension(300, 250));
        panel.add(pieChart, BorderLayout.CENTER);
        
        // Store reference for updates
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
        
        // Custom Bar Chart Panel
        AgeBarChart barChart = new AgeBarChart();
        barChart.setPreferredSize(new Dimension(300, 250));
        panel.add(barChart, BorderLayout.CENTER);
        
        // Store reference for updates
        panel.putClientProperty("barChart", barChart);
        
        return panel;
    }
    
    private JLabel addStatsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(108, 117, 125));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        statsPanel.add(card);
        return valueLabel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("BARANGAY DASHBOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_BLUE);
        
        JLabel subtitleLabel = new JLabel("Population Statistics Overview", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void refreshData() {
        int total = dao.getTotalResidents();
        int male = dao.getMaleCount();
        int female = dao.getFemaleCount();
        int voters = dao.getVoterCount();
        int children = dao.getChildrenCount();
        int youth = dao.getYouthCount();
        int adult = total - children - youth - dao.getSeniorCitizenCount();
        int seniors = dao.getSeniorCitizenCount();
        
        // Update stat cards
        totalCountLabel.setText(String.valueOf(total));
        maleCountLabel.setText(String.valueOf(male));
        femaleCountLabel.setText(String.valueOf(female));
        voterCountLabel.setText(String.valueOf(voters));
        
        // Update gender pie chart
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
    }
    
    // Custom Pie Chart for Gender Distribution
    private class GenderPieChart extends JPanel {
        private int maleCount = 0;
        private int femaleCount = 0;
        
        public GenderPieChart() {
            setBackground(CARD_BG);
            setPreferredSize(new Dimension(300, 250));
        }
        
        public void updateData(int male, int female) {
            this.maleCount = male;
            this.femaleCount = female;
            repaint();
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
            double femaleAngle = 360.0 * femaleCount / total;
            
            // Draw Male slice
            g2.setColor(ACCENT_BLUE);
            g2.fillArc(x, y, size, size, 0, (int) maleAngle);
            
            // Draw Female slice
            g2.setColor(ACCENT_RED);
            g2.fillArc(x, y, size, size, (int) maleAngle, (int) femaleAngle);
            
            // Draw border
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, size, size);
            
            // Draw legend
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
    
    // Custom Bar Chart for Age Distribution
    private class AgeBarChart extends JPanel {
        private int children = 0;
        private int youth = 0;
        private int adult = 0;
        private int seniors = 0;
        
        private final String[] AGE_GROUPS = {"0-14", "15-30", "31-59", "60+"};
        private final Color[] BAR_COLORS = {ACCENT_TEAL, ACCENT_YELLOW, ACCENT_GREEN, ACCENT_RED};
        
        public AgeBarChart() {
            setBackground(CARD_BG);
            setPreferredSize(new Dimension(300, 250));
        }
        
        public void updateData(int children, int youth, int adult, int seniors) {
            this.children = children;
            this.youth = youth;
            this.adult = adult;
            this.seniors = seniors;
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int[] values = {children, youth, adult, seniors};
            int maxValue = 1;
            for (int v : values) {
                if (v > maxValue) maxValue = v;
            }
            if (maxValue == 0) maxValue = 1;
            
            int chartWidth = getWidth() - 100;
            int chartHeight = getHeight() - 80;
            int barWidth = (chartWidth / AGE_GROUPS.length) - 10;
            int startX = 50;
            int startY = getHeight() - 50;
            
            // Draw axes
            g2.setColor(Color.GRAY);
            g2.drawLine(startX - 10, startY, startX + chartWidth, startY);
            g2.drawLine(startX - 10, startY - chartHeight, startX - 10, startY);
            
            // Draw y-axis labels
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= 4; i++) {
                int y = startY - (i * chartHeight / 4);
                int value = (maxValue * i / 4);
                g2.drawString(String.valueOf(value), startX - 30, y + 3);
            }
            
            // Draw bars
            for (int i = 0; i < AGE_GROUPS.length; i++) {
                int barHeight = (int) ((double) values[i] / maxValue * chartHeight);
                if (barHeight < 5 && values[i] > 0) barHeight = 5;
                int x = startX + i * (barWidth + 10);
                int y = startY - barHeight;
                
                // Draw bar
                g2.setColor(BAR_COLORS[i]);
                g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                
                // Draw value on top of bar
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                g2.drawString(String.valueOf(values[i]), x + barWidth / 2 - 10, y - 5);
                
                // Draw label
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                g2.drawString(AGE_GROUPS[i], x + barWidth / 2 - 15, startY + 15);
            }
            
            g2.dispose();
        }
    }
}