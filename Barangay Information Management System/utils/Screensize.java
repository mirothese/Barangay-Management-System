// utils/ScreenSize.java
package utils;

import java.awt.*;

public class ScreenSize {
    
    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    
    public static int getScreenWidth() {
        return getScreenSize().width;
    }
    
    public static int getScreenHeight() {
        return getScreenSize().height;
    }
    
    public static void centerWindow(Window window) {
        Dimension screenSize = getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
    
    public static void setWindowSize(Window window, double percentage) {
        Dimension screenSize = getScreenSize();
        int width = (int) (screenSize.width * percentage);
        int height = (int) (screenSize.height * percentage);
        window.setSize(width, height);
        centerWindow(window);
    }
}