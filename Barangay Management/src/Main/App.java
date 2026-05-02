package Main;

import java.awt.*;

public class App extends AppPanel{

    private AppFrame appframe;
    private AppPanel apppanel;

    public App(){
        apppanel = new AppPanel();
        apppanel.setLayout(new BorderLayout(2,0));
        apppanel.setBackground(Color.BLACK);
        apppanel.add(lefttoppanel, BorderLayout.WEST);
        apppanel.add(mainpanel, BorderLayout.CENTER);
        appframe = new AppFrame(apppanel);
    }
}
