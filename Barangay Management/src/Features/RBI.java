package Features;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.Color;

public class RBI extends JPanel{

    public String firstName, middleName, lastName, birth, age, sex, civilStatus, contactNumber,houseNumber, occupation;

    Color color1 = new Color(204, 218, 227);
    Font myFont = new Font("Arial", Font.BOLD, 60);

    JLabel title = new JLabel("<html><center>Residence of<br>Barangay Inhabitants</center></html>");

    JTextArea residences = new JTextArea();
    JTextArea blank1 = new JTextArea();

    JButton newResidenceButton = new JButton();

    JScrollPane residenceList = new JScrollPane(residences);

    JPanel appointPanel = new JPanel();
    JPanel topPanel = new JPanel();
    JPanel botPanel = new JPanel();
    JPanel top_botPanel = new JPanel();
    JPanel bot_botPanel = new JPanel();
    JPanel buttons_bot_botPanel = new JPanel();

    JOptionPane inputPane = new JOptionPane();

    public void updateResidenceLists(){
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\RBI\\RBIdata.dat"))){
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
                residences.append(line+"\n");
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("Could not locate file location");
        }
        catch (IOException e) {
            System.out.println("Could not write file");
        }
    }

    public void updateDisplay(){
        appointPanel.revalidate();
        appointPanel.repaint();
    }

    public void click(JButton btn){
        inputPane.setIcon(null);
        firstName =     inputPane.showInputDialog("First name: ");
        middleName =    inputPane.showInputDialog("Middle Name: ");
        lastName =      inputPane.showInputDialog("Last Name: ");
        birth =         inputPane.showInputDialog("Date of Birth:");
        age =           inputPane.showInputDialog("Age: ");
        sex =           inputPane.showInputDialog("Sex: ");
        civilStatus =   inputPane.showInputDialog("Civil Status: ");
        contactNumber = inputPane.showInputDialog("Contact Number: ");
        houseNumber =   inputPane.showInputDialog("House Number: ");
        occupation =    inputPane.showInputDialog("Occupation: ");

        try {
            BufferedWriter RBIdata = new BufferedWriter(new FileWriter("src\\RBI\\RBIdata.dat", true));
            RBIdata.append("First Name:\t\t").append(firstName).append("\n");
            RBIdata.append("Middle Name:\t\t").append(middleName).append("\n");
            RBIdata.append("Last Name:\t\t").append(lastName).append("\n");
            RBIdata.append("Birth Date:\t\t").append(birth).append("\n");
            RBIdata.append("Age:\t\t").append(age).append("\n");
            RBIdata.append("Sex:\t\t").append(sex).append("\n");
            RBIdata.append("Civil Status:\t\t").append(civilStatus).append("\n");
            RBIdata.append("Contact Number:\t").append(contactNumber).append("\n");
            RBIdata.append("House Number:\t").append(houseNumber).append("\n");
            RBIdata.append("Occupation:\t\t").append(occupation).append("\n");

            RBIdata.close();
        } catch (IOException e) {
            System.out.println("Could not write file");
        }

        updateResidenceLists();

        updateDisplay();
    }

    public void Visuals(){
        appointPanel.setLayout(new BorderLayout());

        title.setFont(myFont);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(0,0));

        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(color1);
        topPanel.add(title);
        topPanel.setPreferredSize(new Dimension(0, 150));

        newResidenceButton.setText("New Resident");
        newResidenceButton.setFont(new Font("Arial", Font.BOLD, 25));
        newResidenceButton.setPreferredSize(new Dimension(200,70));
        newResidenceButton.setBorder(BorderFactory.createRaisedBevelBorder());
        newResidenceButton.setFocusPainted(false);
        newResidenceButton.addActionListener(e -> click(newResidenceButton));

        blank1.setBackground(color1);
        blank1.setBorder(BorderFactory.createEmptyBorder());
        blank1.setPreferredSize(new Dimension(685,0));

        buttons_bot_botPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 23,15));
        buttons_bot_botPanel.setBackground(color1);
        buttons_bot_botPanel.add(newResidenceButton);
        buttons_bot_botPanel.add(blank1);

        residences.setFont(new Font("Arial", Font.BOLD, 25));
        residences.setLineWrap(true);
        residenceList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        residenceList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        top_botPanel.setLayout(new BorderLayout());
        top_botPanel.setBackground(color1);
        top_botPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
        top_botPanel.setPreferredSize(new Dimension(900,450));
        top_botPanel.add(residenceList);

        updateResidenceLists();

        bot_botPanel.setLayout(new BorderLayout());
        bot_botPanel.setBackground(color1);
        bot_botPanel.setPreferredSize(new Dimension(0,90));

        botPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        botPanel.setBackground(color1);
        botPanel.add(top_botPanel);
        botPanel.add(bot_botPanel);
        botPanel.add(buttons_bot_botPanel);
        botPanel.setPreferredSize(new Dimension(0, 0));

        appointPanel.add(topPanel, BorderLayout.NORTH);
        appointPanel.add(botPanel, BorderLayout.CENTER);

        appointPanel.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(appointPanel);
    }

    public RBI(){
        Visuals();
    }
}
