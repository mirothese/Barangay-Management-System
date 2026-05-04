// Main.java
import ui.DashboardUI;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch dashboard
        SwingUtilities.invokeLater(() -> {
            new DashboardUI().setVisible(true);
        });
    }
}