package Main;

import java.awt.*;
import javax.swing.*;
import Features.*;
import Source.*;
import java.io.*;

import javax.swing.JPanel;

public class AppPanel extends JPanel {

    public ScreenSize screenSize = new ScreenSize();

    public ImageIcon iconImage = new ImageIcon("iconMenu.png");

    public Font myFont = new Font("Arial", Font.BOLD, 18);
    public Font menuFont = new Font("Arial", Font.BOLD, 30);

    public Color color1 = new Color(204, 218, 227);
    public Color color2 = new Color(30,30,30);
    public Color color3 = Color.WHITE;

    public JPanel leftpanel = new JPanel();
    public JPanel lefttoppanel = new JPanel();
    public JPanel leftbotpanel = new JPanel();
    public JPanel mainpanel = new JPanel();
    public JPanel mainpanelText = new JPanel(new FlowLayout(FlowLayout.CENTER));

    public JLabel iconMenu = new JLabel();
    public JLabel iconText = new JLabel("BARANGAY MANAGEMENT SYSTEM");
    public JLabel regionTextArea = new JLabel("Region: None");

    public JLabel menuTitle = new JLabel(
            "<html><center>Barangay<br>Information<br>" +
                                "Management<br>" +
                                "System</center></html>");

    public JButton RBIButt = new JButton(
            "<html><center>Records of<br>" +
                                "Barangay<br>" +
                                "Inhabitants</center></html>");
    public JButton DashButt = new JButton(
            "<html><center>Dashboard</center></html>");
    public JButton setRegionButt = new JButton("Set Region");

    public File region = new File("src/Main/region.dat");

    public AppPanel(){
        setPanelSize();
        setRegionText();
        AppLayout();
        AppLeftButtons();
    }

    public void AppLayout(){

        lefttoppanel.setPreferredSize(new Dimension(300,0));
        lefttoppanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        lefttoppanel.setBackground(color2);

        leftbotpanel.setPreferredSize(new Dimension(300,100));
        leftbotpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftbotpanel.setBackground(color2);

        leftpanel.setLayout(new BorderLayout());
        leftpanel.add(lefttoppanel, BorderLayout.CENTER);
        leftpanel.add(leftbotpanel, BorderLayout.SOUTH);
        leftpanel.setBackground(color2);

        menuTitle.setFont(menuFont);
        menuTitle.setForeground(color3);

        lefttoppanel.add(menuTitle);
        lefttoppanel.add(Box.createVerticalStrut(240));

        leftbotpanel.add(regionTextArea);

        iconMenu.setIcon(iconImage);

        mainpanelText.setBackground(color1);
        mainpanelText.add(iconMenu);

        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(mainpanelText, BorderLayout.CENTER);
    }

    public void AppLeftButtons(){
        RBIButt.setBorder(BorderFactory.createRaisedBevelBorder());
        RBIButt.setFocusPainted(false);
        RBIButt.setPreferredSize(new Dimension(250,80));
        RBIButt.setFont(myFont);
        RBIButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed RBI");
            mainpanel.add(new RBI());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        DashButt.setBorder(BorderFactory.createRaisedBevelBorder());
        DashButt.setFocusPainted(false);
        DashButt.setPreferredSize(new Dimension(250,40));
        DashButt.setFont(myFont);
        DashButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed dashboard");
            mainpanel.add(new Dashboard());
            mainpanel.revalidate();
            mainpanel.repaint();
        });
        String regionInput;
        setRegionButt.setBorder(BorderFactory.createRaisedBevelBorder());
        setRegionButt.setFocusPainted(false);
        setRegionButt.setPreferredSize(new Dimension(270, 40));
        setRegionButt.setFont(myFont);
        setRegionButt.addActionListener(e -> {
            String inputDialog = JOptionPane.showInputDialog(this, "Enter Region:");

            if (inputDialog != null && !inputDialog.trim().isEmpty()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Main/region.dat"))) {
                    bw.write(inputDialog);
                    regionTextArea.setText(inputDialog);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to save region: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        lefttoppanel.add(RBIButt);
        lefttoppanel.add(DashButt);
        leftbotpanel.add(setRegionButt);
    }

    private void setRegionText() {
        regionTextArea.setFont(myFont);
        regionTextArea.setForeground(color3);

        if (!region.exists()) {
            regionTextArea.setText("Region: None");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(region))) {
            String line = br.readLine();
            if (line != null) {
                regionTextArea.setText(line);
            } else {
                regionTextArea.setText("Region: None");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading region file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            regionTextArea.setText("Region: Error");
        }
    }

    private void setPanelSize(){
        Dimension newsize = screenSize.getNewSize();
        setMinimumSize(newsize);
        setPreferredSize(newsize);
        setMaximumSize(newsize);
    }
}
