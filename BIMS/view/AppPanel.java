// view/AppPanel.java
package view;

import java.awt.*;
import javax.swing.*;
import controller.Dashboard;
import utils.ScreenSize;
import components.ResidentPanel;
import java.io.*;

public class AppPanel extends JPanel {
    
    public ScreenSize screenSize = new ScreenSize();
    public ImageIcon iconImage = new ImageIcon("resources/images/iconMenu.png");

    public Font myFont = new Font("Arial", Font.PLAIN, 18);
    public Font menuFont = new Font("Arial", Font.BOLD, 30);

    public Color color1 = new Color(204, 218, 227);
    public Color color2 = new Color(30, 30, 30);
    public Color color3 = Color.WHITE;

    public JPanel leftpanel = new JPanel();
    public JPanel lefttoppanel = new JPanel();
    public JPanel leftbotpanel = new JPanel();
    public JPanel mainpanel = new JPanel();
    public JPanel mainpanelText = new JPanel(new FlowLayout(FlowLayout.CENTER));

    public JLabel iconMenu = new JLabel();

    public JLabel provinceLabel = new JLabel("Province:          None");
    public JLabel cityLabel = new JLabel("City/Municipality: None");
    public JLabel barangayLabel = new JLabel("Barangay:          None");

    public JLabel menuTitle = new JLabel(
            "<html><center><br><br>Barangay<br>Information<br>" +
                    "Management<br>" +
                    "System<br></center></html>");
    
    public JButton btnDashboard = new JButton(
            "<html><center>Dashboard</center></html>");
    
    public JButton btnResidentDatabase = new JButton(
            "<html><center>Barangay Inhabitant<br>" +
                    "Profiling Database</center></html>");

    public JButton btnSetLocation = new JButton("Set Location");

    public File locationFile = new File("data/location.dat");
    
    private ResidentPanel residentPanel;
    private Dashboard dashboardPanel;

    public AppPanel() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        setPanelSize();
        loadLocationData();
        AppLayout();
        AppLeftButtons();
        
        residentPanel = new ResidentPanel();
        dashboardPanel = new Dashboard();
        
        showDashboard();
        
        // Add shutdown hook to save data before exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving data before exit...");
        }));
    }

    public void AppLayout() {
        lefttoppanel.setPreferredSize(new Dimension(300, 0));
        lefttoppanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        lefttoppanel.setBackground(color2);

        leftbotpanel.setPreferredSize(new Dimension(300, 180));
        leftbotpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftbotpanel.setBackground(color2);

        leftpanel.setLayout(new BorderLayout());
        leftpanel.add(lefttoppanel, BorderLayout.CENTER);
        leftpanel.add(leftbotpanel, BorderLayout.SOUTH);
        leftpanel.setBackground(color2);

        menuTitle.setFont(menuFont);
        menuTitle.setForeground(color3);
        lefttoppanel.add(menuTitle);
        lefttoppanel.add(Box.createVerticalStrut(300));

        styleLocationLabel(provinceLabel);
        styleLocationLabel(cityLabel);
        styleLocationLabel(barangayLabel);

        provinceLabel.setPreferredSize(new Dimension(280, 40));
        cityLabel.setPreferredSize(new Dimension(280, 40));
        barangayLabel.setPreferredSize(new Dimension(280, 40));

        leftbotpanel.add(provinceLabel);
        leftbotpanel.add(cityLabel);
        leftbotpanel.add(barangayLabel);

        iconMenu.setIcon(iconImage);
        mainpanelText.setBackground(color1);
        mainpanelText.add(iconMenu);

        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(mainpanelText, BorderLayout.CENTER);
        mainpanel.setBackground(Color.WHITE);
    }

    private void styleLocationLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(color3);
    }
    
    private void showDashboard() {
        mainpanel.removeAll();
        mainpanel.add(dashboardPanel);
        mainpanel.revalidate();
        mainpanel.repaint();
    }
    
    private void showResidentPanel() {
        mainpanel.removeAll();
        mainpanel.add(residentPanel);
        mainpanel.revalidate();
        mainpanel.repaint();
    }

    public void AppLeftButtons() {
        btnDashboard.setBackground(color3);
        btnDashboard.setBorder(BorderFactory.createRaisedBevelBorder());
        btnDashboard.setFocusPainted(false);
        btnDashboard.setPreferredSize(new Dimension(250, 50));
        btnDashboard.setFont(myFont);
        btnDashboard.addActionListener(e -> showDashboard());

        btnResidentDatabase.setBackground(color3);
        btnResidentDatabase.setBorder(BorderFactory.createRaisedBevelBorder());
        btnResidentDatabase.setFocusPainted(false);
        btnResidentDatabase.setPreferredSize(new Dimension(250, 80));
        btnResidentDatabase.setFont(myFont);
        btnResidentDatabase.addActionListener(e -> showResidentPanel());

        btnSetLocation.setBackground(color3);
        btnSetLocation.setBorder(BorderFactory.createRaisedBevelBorder());
        btnSetLocation.setFocusPainted(false);
        btnSetLocation.setPreferredSize(new Dimension(270, 40));
        btnSetLocation.setFont(myFont);
        btnSetLocation.addActionListener(e -> openLocationDialog());

        lefttoppanel.add(btnDashboard);
        lefttoppanel.add(btnResidentDatabase);
        leftbotpanel.add(btnSetLocation);
    }

    private void openLocationDialog() {
        String currentProvince = provinceLabel.getText().replace("Province: ", "").trim();
        String currentCity = cityLabel.getText().replace("City/Municipality: ", "").trim();
        String currentBarangay = barangayLabel.getText().replace("Barangay: ", "").trim();

        if (currentProvince.equals("None")) currentProvince = "";
        if (currentCity.equals("None")) currentCity = "";
        if (currentBarangay.equals("None")) currentBarangay = "";

        String province = JOptionPane.showInputDialog(this, "Enter Province:", currentProvince);
        if (province == null)
            return;

        String city = JOptionPane.showInputDialog(this, "Enter City/Municipality:", currentCity);
        if (city == null)
            return;

        String barangay = JOptionPane.showInputDialog(this, "Enter Barangay:", currentBarangay);
        if (barangay == null)
            return;

        updateLabels(province, city, barangay);
        saveLocationData(province, city, barangay);
    }

    private void updateLabels(String p, String c, String b) {
        provinceLabel.setText("Province: " + (p.trim().isEmpty() ? "None" : p.trim()));
        cityLabel.setText("City/Municipality: " + (c.trim().isEmpty() ? "None" : c.trim()));
        barangayLabel.setText("Barangay: " + (b.trim().isEmpty() ? "None" : b.trim()));
    }

    private void saveLocationData(String province, String city, String barangay) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(locationFile))) {
            bw.write(province.trim());
            bw.newLine();
            bw.write(city.trim());
            bw.newLine();
            bw.write(barangay.trim());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save location: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLocationData() {
        if (!locationFile.exists()) {
            updateLabels("", "", "");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(locationFile))) {
            String province = br.readLine();
            String city = br.readLine();
            String barangay = br.readLine();
            updateLabels(
                    province != null ? province : "",
                    city != null ? city : "",
                    barangay != null ? barangay : "");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading location file: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            updateLabels("", "", "");
        }
    }

    private void setPanelSize() {
        Dimension newsize = screenSize.getNewSize();
        setMinimumSize(newsize);
        setPreferredSize(newsize);
        setMaximumSize(newsize);
    }
}