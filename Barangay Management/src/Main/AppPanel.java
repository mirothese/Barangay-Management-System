package Main;

import java.awt.*;
import javax.swing.*;
import Features.*;
import inputs.*;

import javax.swing.JPanel;

public class AppPanel extends JPanel {

    ScreenSize screenSize = new ScreenSize();

    public JPanel leftpanel = new JPanel();
    public JPanel lefttoppanel = new JPanel();
    public JPanel leftbotpanel = new JPanel();
    public JPanel mainpanel = new JPanel();

    Color color1 = new Color(204, 218, 227);

    JButton RBIButt = new JButton("<html><center>Records of<br>Barangay<br>Inhabitants</center></html>");
    JButton SuppliesButt = new JButton("<html><center>Inventory of<br>Supplies</center></html>");
    JButton EquipmentButt = new JButton("<html><center>Inventory of<br>Equipment</center></html>");
    JButton ResolutionButt = new JButton("<html><center>Resolution<br>Tracking</center></html>");
    JButton OrdinanceButt = new JButton("<html><center>Ordinance<br>Tracking</center></html>");

    JLabel menuTitle = new JLabel("<html><center>Barangay<br>Information<br>Management<br>System</center></html>");

    public Font myFont = new Font("Arial", Font.BOLD, 18);
    public Font menuFont = new Font("Arial", Font.BOLD, 30);



    public AppPanel(){
        setPanelSize();
        AppLayout();
        AppLeftButtons();
    }

    public void AppLayout(){

        lefttoppanel.setPreferredSize(new Dimension(300,0));
        lefttoppanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        lefttoppanel.setBackground(color1);

        leftbotpanel.setPreferredSize(new Dimension(0,0));
        leftbotpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        leftbotpanel.setBackground(color1);;

        leftpanel.setLayout(new BorderLayout());
        leftpanel.add(lefttoppanel, BorderLayout.CENTER);
        leftpanel.add(leftbotpanel, BorderLayout.SOUTH);
        leftpanel.setBackground(color1);

        menuTitle.setFont(menuFont);

        lefttoppanel.add(menuTitle);
        lefttoppanel.add(Box.createVerticalStrut(240));

        mainpanel.setLayout(new BorderLayout());
        mainpanel.setBackground(color1);
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

        SuppliesButt.setBorder(BorderFactory.createRaisedBevelBorder());
        SuppliesButt.setFocusPainted(false);
        SuppliesButt.setPreferredSize(new Dimension(250,60));
        SuppliesButt.setFont(myFont);
        SuppliesButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed supplies");
            mainpanel.add(new InventoryOfSupplies());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        EquipmentButt.setBorder(BorderFactory.createRaisedBevelBorder());
        EquipmentButt.setFocusPainted(false);
        EquipmentButt.setPreferredSize(new Dimension(250,60));
        EquipmentButt.setFont(myFont);
        EquipmentButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed equpiment");
            mainpanel.add(new InventoryOfEquipment());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        ResolutionButt.setBorder(BorderFactory.createRaisedBevelBorder());
        ResolutionButt.setFocusPainted(false);
        ResolutionButt.setPreferredSize(new Dimension(250,60));
        ResolutionButt.setFont(myFont);
        ResolutionButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed resolution");
            mainpanel.add(new ResolutionTracking());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        OrdinanceButt.setBorder(BorderFactory.createRaisedBevelBorder());
        OrdinanceButt.setFocusPainted(false);
        OrdinanceButt.setPreferredSize(new Dimension(250,60));
        OrdinanceButt.setFont(myFont);
        OrdinanceButt.addActionListener(_ -> {
            mainpanel.removeAll();
            System.out.println("pressed ordinance");
            mainpanel.add(new OrdinanceTracking());
            mainpanel.revalidate();
            mainpanel.repaint();
        });

        lefttoppanel.add(RBIButt);
        lefttoppanel.add(SuppliesButt);
        lefttoppanel.add(EquipmentButt);
        lefttoppanel.add(ResolutionButt);
        lefttoppanel.add(OrdinanceButt);
    }
    private void setPanelSize(){
        Dimension newsize = screenSize.getNewSize();
        setMinimumSize(newsize);
        setPreferredSize(newsize);
        setMaximumSize(newsize);
    }
}
