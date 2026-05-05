package utils;

import java.awt.*;

public class ScreenSize {
    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    int widths = (screensize.width / 2) + (screensize.width / 3);
    int heights = (screensize.height / 2) + (screensize.height / 3);
    Dimension newsize = new Dimension(widths, heights);

    public Dimension getNewSize() {
        return newsize;
    }
}