package Main;

import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;

public class AppFrame {

    public AppFrame(AppPanel apppanel){

        JFrame jframe = new JFrame("Barangay Management");

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(apppanel);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

    }
}
