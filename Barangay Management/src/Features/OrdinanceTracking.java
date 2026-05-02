package Features;

import javax.swing.*;
import java.awt.*;

public class OrdinanceTracking extends JPanel{
    Color color1 = new Color(204, 218, 227);
    Font myFont = new Font("Arial", Font.BOLD, 60);

    JLabel title = new JLabel("Prescription Tracking");

    JTextField textField = new JTextField();
    JButton textField_Button = new JButton();

    JPanel appointPanel = new JPanel();
    JPanel topPanel = new JPanel();
    JPanel botPanel = new JPanel();
    JPanel bot_botPanel = new JPanel();
    JPanel textarea1_bot_botPanel = new JPanel();
    JPanel divpanel1 = new JPanel();
    JPanel divpanel2 = new JPanel();

    public OrdinanceTracking(){
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

        divpanel2.setLayout(new BorderLayout());
        divpanel2.setBackground(Color.BLACK);
        divpanel2.setPreferredSize(new Dimension(0,2));

        textField_Button.setText("SUBMIT");
        textField_Button.setFont(new Font("Arial", Font.BOLD, 30));
        textField_Button.setPreferredSize(new Dimension(200,70));
        textField_Button.setBorder(BorderFactory.createRaisedBevelBorder());

        textField.setPreferredSize(new Dimension(700,70));
        textField.setFont(new Font("Arial", Font.BOLD, 30));

        textarea1_bot_botPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 23,15));
        textarea1_bot_botPanel.setBackground(color1);
        textarea1_bot_botPanel.add(textField_Button);
        textarea1_bot_botPanel.add(textField);

        bot_botPanel.setLayout(new BorderLayout());
        bot_botPanel.setBackground(color1);
        bot_botPanel.add(divpanel2, BorderLayout.NORTH);
        bot_botPanel.add(textarea1_bot_botPanel, BorderLayout.CENTER);
        bot_botPanel.setPreferredSize(new Dimension(0,100));

        botPanel.setLayout(new BorderLayout());
        botPanel.setBackground(color1);
        botPanel.add(bot_botPanel, BorderLayout.SOUTH);
        botPanel.setPreferredSize(new Dimension(0, 0));

        topPanel.add(divpanel1, BorderLayout.SOUTH);
        appointPanel.add(topPanel, BorderLayout.NORTH);
        appointPanel.add(botPanel, BorderLayout.CENTER);

        appointPanel.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(appointPanel);
    }
}

