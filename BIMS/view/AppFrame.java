// view/AppFrame.java
package view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AppFrame {

    private JFrame jframe;
    private boolean isFullScreen = false;
    private GraphicsDevice device;
    private Rectangle normalBounds;

    public AppFrame(AppPanel apppanel) {
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        jframe = new JFrame("Barangay Management System");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(apppanel);
        
        // Set initial size
        jframe.setSize(1200, 800);
        jframe.setLocationRelativeTo(null);
        
        // Add keyboard shortcut F11 to toggle full screen
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11 && e.getID() == KeyEvent.KEY_RELEASED) {
                    toggleFullScreen();
                }
                return false;
            }
        });
        
        jframe.setVisible(true);
    }
    
    private void toggleFullScreen() {
        if (isFullScreen) {
            // Exit full screen
            device.setFullScreenWindow(null);
            jframe.dispose();
            jframe.setUndecorated(false);
            jframe.setBounds(normalBounds);
            jframe.setVisible(true);
            isFullScreen = false;
        } else {
            // Enter full screen
            normalBounds = jframe.getBounds();
            jframe.dispose();
            jframe.setUndecorated(true);
            device.setFullScreenWindow(jframe);
            isFullScreen = true;
        }
    }
}