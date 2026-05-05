package view;

import java.awt.*;
import javax.swing.*;
import controller.Dashboard;
import utils.ScreenSize;
import components.ResidentPanel;

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

    public JLabel menuTitle = new JLabel(
            "<html><center><br><br>Barangay<br>Information<br>" +
                    "Management<br>" +
                    "System<br></center></html>");
    
    public JButton btnDashboard = new JButton(
            "<html><center>Dashboard</center></html>");
    
    public JButton btnResidentDatabase = new JButton(
            "<html><center>Barangay Inhabitant<br>" +
                    "Profiling Database</center></html>");

    private ResidentPanel residentPanel;
    private Dashboard dashboardPanel;
    
    public AppPanel() {
        java.io.File dataDir = new java.io.File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        setPanelSize();
        AppLayout();
        AppLeftButtons();
        
        residentPanel = new ResidentPanel();
        dashboardPanel = new Dashboard();
        
        residentPanel.setParentAppPanel(this);
        
        showDashboard();
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

        iconMenu.setIcon(iconImage);
        mainpanelText.setBackground(color1);
        mainpanelText.add(iconMenu);

        mainpanel.setLayout(new BorderLayout());
        mainpanel.add(mainpanelText, BorderLayout.CENTER);
        mainpanel.setBackground(Color.WHITE);
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

    public void refreshDashboard() {
        if (dashboardPanel != null) {
            dashboardPanel.refreshData();
        }
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

        lefttoppanel.add(btnDashboard);
        lefttoppanel.add(btnResidentDatabase);
    }

    private void setPanelSize() {
        Dimension newsize = screenSize.getNewSize();
        setMinimumSize(newsize);
        setPreferredSize(newsize);
        setMaximumSize(newsize);
    }
}