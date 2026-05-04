package Main;

import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;

public class AppFrame {

    ImageIcon icon = new ImageIcon("icon.png");

    public AppFrame(AppPanel apppanel){

        JFrame jframe = new JFrame("Barangay Information Management System (ver. 2026.05.01)");

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(apppanel);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setIconImage(icon.getImage());
        jframe.setVisible(true);

    }
}
