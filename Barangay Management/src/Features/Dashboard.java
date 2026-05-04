package Features;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Dashboard extends JPanel {

    private static final String DATA_FILE = "src/RBI/RBIdata.dat";

    // Colors
    private static final Color BG_COLOR = new Color(245, 249, 252);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color HEADER_COLOR = new Color(44, 62, 80);
    private static final Color TEXT_COLOR = new Color(52, 73, 94);
    private static final Color BAR_COLOR_1 = new Color(52, 152, 219);   // Blue
    private static final Color BAR_COLOR_2 = new Color(46, 204, 113);   // Green
    private static final Color BAR_COLOR_3 = new Color(155, 89, 182);   // Purple

    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font VALUE_FONT = new Font("Segoe UI", Font.BOLD, 26);

    // UI Components
    private JLabel totalCountLabel, maleCountLabel, femaleCountLabel, voterCountLabel;
    private JPanel statsPanel;
    private JPanel chartsPanel;
    private JPanel sectorChartPanel;
    private JPanel genderChartPanel;

    public Dashboard() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel header = new JLabel("Barangay Inhabitants Dashboard");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(HEADER_COLOR);
        headerPanel.add(header, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // --- Stats Cards ---
        statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setOpaque(false);

        addStatsCard("Total Residents", "0");
        addStatsCard("Males", "0");
        addStatsCard("Females", "0");
        addStatsCard("Registered Voters", "0");

        add(statsPanel, BorderLayout.NORTH);

        // --- Charts Area ---
        chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setOpaque(false);

        // Initialize with empty data first
        Map<String, Integer> emptyMap = new HashMap<>();

        sectorChartPanel = createCustomChartPanel("Sectoral Groups", BAR_COLOR_1, emptyMap);
        genderChartPanel = createCustomChartPanel("Gender Distribution", BAR_COLOR_1, emptyMap);

        chartsPanel.add(sectorChartPanel);
        chartsPanel.add(genderChartPanel);

        add(chartsPanel, BorderLayout.CENTER);

        // Load data
        refreshData();
    }

    private JPanel createCustomChartPanel(String title, Color barColor, Map<String, Integer> data) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(true);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 230), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        wrapper.add(titleLabel, BorderLayout.NORTH);

        // Custom Chart Panel
        ChartPanel chartPanel = new ChartPanel(data, barColor);
        chartPanel.setPreferredSize(new Dimension(0, 250));

        wrapper.add(chartPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private void addStatsCard(String title, String initialValue) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 240, 245), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setOpaque(true);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(LABEL_FONT);
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(initialValue);
        valueLabel.setFont(VALUE_FONT);
        valueLabel.setForeground(ACCENT_COLOR);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        statsPanel.add(card);

        // Store references
        int index = statsPanel.getComponentCount() - 1;
        if (index == 0) totalCountLabel = valueLabel;
        else if (index == 1) maleCountLabel = valueLabel;
        else if (index == 2) femaleCountLabel = valueLabel;
        else if (index == 3) voterCountLabel = valueLabel;
    }

    public void refreshData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            resetStats();
            return;
        }

        int total = 0, males = 0, females = 0, voters = 0;
        Map<String, Integer> sectors = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentSex = "";
            String currentVoter = "";
            String currentSector = "";
            boolean inRecord = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("First Name")) {
                    inRecord = true;
                    total++;
                } else if (line.trim().equals("--------------------------------------------------")) {
                    if (currentSex.equals("MALE")) males++;
                    else if (currentSex.equals("FEMALE")) females++;

                    if (currentVoter.equals("YES")) voters++;

                    if (!currentSector.isEmpty()) {
                        sectors.put(currentSector, sectors.getOrDefault(currentSector, 0) + 1);
                    }

                    currentSex = "";
                    currentVoter = "";
                    currentSector = "";
                    inRecord = false;
                } else if (inRecord) {
                    if (line.startsWith("Sex:")) currentSex = line.replace("Sex:", "").trim();
                    if (line.startsWith("Resident Voter:")) currentVoter = line.replace("Resident Voter:", "").trim();
                    if (line.startsWith("Sectoral Group:")) currentSector = line.replace("Sectoral Group:", "").trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading data file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Update Labels
        totalCountLabel.setText(String.valueOf(total));
        maleCountLabel.setText(String.valueOf(males));
        femaleCountLabel.setText(String.valueOf(females));
        voterCountLabel.setText(String.valueOf(voters));

        // Update Charts
        updateChart(sectorChartPanel, sectors);
        updateChart(genderChartPanel, Map.of("Male", males, "Female", females));
    }

    private void updateChart(JPanel wrapperPanel, Map<String, Integer> newData) {
        // Get the inner ChartPanel
        Component innerPanel = wrapperPanel.getComponent(1);
        if (innerPanel instanceof ChartPanel) {
            ((ChartPanel) innerPanel).updateData(newData);
        }
    }

    private void resetStats() {
        if (totalCountLabel != null) {
            totalCountLabel.setText("0");
            maleCountLabel.setText("0");
            femaleCountLabel.setText("0");
            voterCountLabel.setText("0");
        }
        updateChart(sectorChartPanel, new HashMap<>());
        updateChart(genderChartPanel, new HashMap<>());
    }

    // --- Custom Chart Panel Class ---
    private static class ChartPanel extends JPanel {
        private Map<String, Integer> data = new HashMap<>();
        private Color barColor;
        private int maxVal = 1;

        public ChartPanel(Map<String, Integer> initialData, Color color) {
            this.barColor = color;
            this.data = new HashMap<>(initialData);
            calculateMax();
        }

        public void updateData(Map<String, Integer> newData) {
            this.data = new HashMap<>(newData);
            calculateMax();
            repaint();
            revalidate();
        }

        private void calculateMax() {
            maxVal = 1;
            for (int val : data.values()) {
                if (val > maxVal) maxVal = val;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (data.isEmpty()) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.drawString("No data available", getWidth() / 2 - 50, getHeight() / 2);
                g2.dispose();
                return;
            }

            int padding = 40; // Left/Right padding for labels
            int chartWidth = getWidth() - (padding * 2);
            int chartHeight = getHeight() - 50; // Leave space for labels

            // FIX: Explicit cast to int to resolve double/int mismatch
            int barWidth = (int) (chartWidth / (data.size() * 1.5));
            int gap = barWidth / 2;

            int i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                String label = entry.getKey();
                int value = entry.getValue();

                // Calculate bar height
                int barHeight = (int) ((value / (double) maxVal) * (chartHeight - 20));
                if (barHeight < 10) barHeight = 10; // Min height

                int x = padding + (i * (barWidth + gap));
                int y = getHeight() - padding - barHeight;

                // Draw Bar with rounded corners
                g2.setColor(barColor);
                g2.fillRoundRect(x, y, barWidth, barHeight, 5, 5);

                // Draw Value on top
                g2.setColor(TEXT_COLOR);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString(String.valueOf(value), x + (barWidth / 2) - 5, y - 5);

                // Draw Label (X-axis)
                g2.setColor(TEXT_COLOR);
                g2.setFont(LABEL_FONT);
                // Truncate long labels
                String displayLabel = label.length() > 12 ? label.substring(0, 10) + "..." : label;
                g2.drawString(displayLabel, x, getHeight() - padding + 15);

                i++;
            }

            g2.dispose();
        }
    }
}