package view;

import java.awt.*;

public class App {
    private AppFrame appframe;
    private AppPanel apppanel;

    public App() {
        apppanel = new AppPanel();
        apppanel.setLayout(new BorderLayout(2, 0));
        apppanel.setBackground(Color.BLACK);
        apppanel.add(apppanel.leftpanel, BorderLayout.WEST);
        apppanel.add(apppanel.mainpanel, BorderLayout.CENTER);

        appframe = new AppFrame(apppanel);
    }
}