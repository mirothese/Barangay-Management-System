package view;

import java.awt.*;
import javax.swing.*;

public class AppFrame {

    ImageIcon icon = new ImageIcon("resources/images/icon.png");

    public AppFrame(AppPanel apppanel) {
        JFrame jframe = new JFrame("Barangay Information Management System");
        jframe.setIconImage(icon.getImage());

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(apppanel);
        jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jframe.setMinimumSize(new Dimension(800, 600));
        jframe.setVisible(true);
    }
}