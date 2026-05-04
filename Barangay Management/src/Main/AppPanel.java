package Main;

import java.awt.*;
import javax.swing.*;
import Features.*;
import Source.*;
import java.io.*;

import javax.swing.JPanel;

public class AppPanel extends JPanel {

    //Get current device screensize
    public ScreenSize screenSize = new ScreenSize();

    //Get image iconMenu.png
    public ImageIcon iconImage = new ImageIcon("iconMenu.png");

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

    //Labels for location names
    public JLabel provinceLabel = new JLabel("Province: None");
    public JLabel cityLabel = new JLabel("City/Mun: None");
    public JLabel barangayLabel = new JLabel("Barangay: None");

    //Three components for the top left area
    public JLabel menuTitle = new JLabel(
            "<html><center><br><br>Barangay<br>Information<br>" +
                    "Management<br>" +
                    "System<br></center></html>");
    public JButton RBIButt = new JButton(
            "<html><center>Barangay Inhabitant<br>" +
                    "Profiling System</center></html>");
    public JButton DashButt = new JButton(
            "<html><center>Dashboard</center></html>");

    //Used for setting location
    public JButton setLocButt = new JButton("Set Location");

    //Used for saving the current location last inputted
    public File locationFile = new File("data/location.dat");

    public AppPanel() {
        // Ensure the data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs(); //Creates the file
        }

        setPanelSize();
        loadLocationData();
        AppLayout();
        AppLeftButtons();
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

        lefttoppanel.add(Box.createVerticalStrut(300)); // Adjusted strut for new layout

        //Style the location labels
        styleLocationLabel(provinceLabel);
        styleLocationLabel(cityLabel);
        styleLocationLabel(barangayLabel);

        //Area to display location Labels
        provinceLabel.setPreferredSize(new Dimension(200,30));
        cityLabel.setPreferredSize(new Dimension(200,30));
        barangayLabel.setPreferredSize(new Dimension(200,30));

        leftbotpanel.add(provinceLabel);
        leftbotpanel.add(cityLabel);
        leftbotpanel.add(barangayLabel);

        //Display iconImage in large area
        iconMenu.setIcon(iconImage);

        mainpanelText.setBackground(color1);
        mainpanelText.add(iconMenu);

        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(mainpanelText, BorderLayout.CENTER);
    }

    private void styleLocationLabel(JLabel label) {
        label.setFont(myFont);
        label.setForeground(color3);
    }

    public void AppLeftButtons() {
        RBIButt.setBackground(color3);
        RBIButt.setBorder(BorderFactory.createRaisedBevelBorder());
        RBIButt.setFocusPainted(false);
        RBIButt.setPreferredSize(new Dimension(250, 80));
        RBIButt.setFont(myFont);
        RBIButt.addActionListener( _ -> {
            mainpanel.removeAll();
            System.out.println("pressed RBI");
            mainpanel.add(new RBI());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        DashButt.setBackground(color3);
        DashButt.setBorder(BorderFactory.createRaisedBevelBorder());
        DashButt.setFocusPainted(false);
        DashButt.setPreferredSize(new Dimension(250, 50));
        DashButt.setFont(myFont);
        DashButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed dashboard");
            mainpanel.add(new Dashboard());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        setLocButt.setBackground(color3);
        setLocButt.setBorder(BorderFactory.createRaisedBevelBorder());
        setLocButt.setFocusPainted(false);
        setLocButt.setPreferredSize(new Dimension(270, 40));
        setLocButt.setFont(myFont);
        setLocButt.addActionListener(e -> {
            openLocationDialog();
        });

        lefttoppanel.add(RBIButt);
        lefttoppanel.add(DashButt);
        leftbotpanel.add(setLocButt);
    }

    private void openLocationDialog() {
        // Get current values to pre-fill if desired, or leave empty
        String currentProvince = provinceLabel.getText().replace("Province: ", "");
        String currentCity = cityLabel.getText().replace("City/Mun: ", "");
        String currentBarangay = barangayLabel.getText().replace("Barangay: ", "");

        if (currentProvince.equals("None")) currentProvince = "";
        if (currentCity.equals("None")) currentCity = "";
        if (currentBarangay.equals("None")) currentBarangay = "";

        String province = JOptionPane.showInputDialog(this, "Enter Province:", currentProvince);
        if (province == null) return; // User cancelled

        String city = JOptionPane.showInputDialog(this, "Enter City/Municipality:", currentCity);
        if (city == null) return;

        String barangay = JOptionPane.showInputDialog(this, "Enter Barangay:", currentBarangay);
        if (barangay == null) return;

        // Update UI immediately
        updateLabels(province, city, barangay);

        // Save to file
        saveLocationData(province, city, barangay);
    }

    private void updateLabels(String p, String c, String b) {
        // .trim() removes after and before spaces
        // .isEmpty() returns true if empty
        provinceLabel.setText("Province: " + (p.trim().isEmpty() ? "None" : p.trim()));
        cityLabel.setText("City/Mun: " + (c.trim().isEmpty() ? "None" : c.trim()));
        barangayLabel.setText("Barangay: " + (b.trim().isEmpty() ? "None" : b.trim()));
    }

    private void saveLocationData(String province, String city, String barangay) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(locationFile))) {
            bw.write(province.trim());  //Overwrites old locations to new locations
            bw.newLine();               //Next line
            bw.write(city.trim());
            bw.newLine();
            bw.write(barangay.trim());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save location: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                    barangay != null ? barangay : ""
            );
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading location file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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