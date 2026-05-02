package Features;

import javax.swing.*;
import java.awt.*;

public class InventoryOfSupplies extends JPanel{
    Color color1 = new Color(204, 218, 227);
    Font myFont = new Font("Arial", Font.BOLD, 60);

    JLabel title = new JLabel("Doctor Records");

    JPanel appointPanel = new JPanel();
    JPanel topPanel = new JPanel();
    JPanel botPanel = new JPanel();
    JPanel divpanel1 = new JPanel();

    public InventoryOfSupplies(){
        appointPanel.setLayout(new BorderLayout());

        title.setFont(myFont);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(0,0));

        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(color1);
        topPanel.add(title);
        topPanel.setPreferredSize(new Dimension(0, 100));

        divpanel1.setLayout(new BorderLayout());
        divpanel1.setBackground(Color.black);
        divpanel1.setPreferredSize(new Dimension(0,2));

        botPanel.setLayout(new BorderLayout());
        botPanel.setBackground(color1);
        botPanel.setPreferredSize(new Dimension(0, 0));

        topPanel.add(divpanel1, BorderLayout.SOUTH);
        appointPanel.add(topPanel, BorderLayout.NORTH);
        appointPanel.add(botPanel, BorderLayout.CENTER);

        appointPanel.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(appointPanel);
    }
}

