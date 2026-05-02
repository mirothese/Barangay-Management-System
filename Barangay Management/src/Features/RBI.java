package Features;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RBI extends JPanel {

    // --- Data Fields ---
    // In a real app, use a Resident class instead of individual strings
    private String firstName, middleName, lastName, birth, age, sex, civilStatus, contactNumber, houseNumber, occupation;

    // --- UI Components ---
    private Color color1 = new Color(204, 218, 227);
    private Font titleFont = new Font("Arial", Font.BOLD, 40); // Reduced size slightly for better fit
    private Font labelFont = new Font("Arial", Font.PLAIN, 14);
    private Font inputFont = new Font("Arial", Font.PLAIN, 14);

    // Main Card Layout Container
    private JPanel cardContainer;
    private CardLayout cardLayout;

    // Card 1: The List View (Your original top_botPanel content)
    private JPanel listPanel;
    private JTextArea residences;
    private JScrollPane residenceList;
    private JButton btnNewResidence;

    // Card 2: The Form View (Replaces JOptionPane)
    private JPanel formPanel;
    private JTextField tfFirstName, tfMiddleName, tfLastName, tfBirth, tfAge, tfSex, tfCivilStatus, tfContact, tfHouse, tfOccupation;
    private JButton btnSave, btnCancel;

    public RBI() {
        // Initialize Layout
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(color1);

        // Build the two "Cards"
        buildListPanel();
        buildFormPanel();

        // Add cards to container
        cardContainer.add(listPanel, "LIST");
        cardContainer.add(formPanel, "FORM");

        // Add container to this RBI panel
        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);

        // Initial load
        updateResidenceLists();
    }

    private void buildListPanel() {
        listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(color1);

        // Title
        JLabel title = new JLabel("<html><center>Residence of<br>Barangay Inhabitants</center></html>");
        title.setFont(titleFont);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(0, 100));
        title.setOpaque(true);
        title.setBackground(color1);

        // List Area
        residences = new JTextArea();
        residences.setFont(new Font("Arial", Font.PLAIN, 14));
        residences.setLineWrap(true);
        residences.setEditable(false);
        residenceList = new JScrollPane(residences);
        residenceList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Button Area
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        btnPanel.setBackground(color1);

        btnNewResidence = new JButton("New Resident");
        btnNewResidence.setFont(new Font("Arial", Font.BOLD, 16));
        btnNewResidence.setPreferredSize(new Dimension(200, 50));
        btnNewResidence.addActionListener(e -> {
            // Switch to Form Card
            cardLayout.show(cardContainer, "FORM");
            // Clear form
            clearForm();
            // Focus first field
            tfFirstName.requestFocus();
        });

        btnPanel.add(btnNewResidence);
        btnPanel.add(new JLabel("   ")); // Spacer

        listPanel.add(title, BorderLayout.NORTH);
        listPanel.add(residenceList, BorderLayout.CENTER);
        listPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void buildFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(color1);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Helper to add label and field
        java.util.function.BiConsumer<String, JTextField> addField = (labelText, field) -> {
            JLabel label = new JLabel(labelText + ":");
            label.setFont(labelFont);
            field.setFont(inputFont);
            field.setPreferredSize(new Dimension(300, 30));

            gbc.gridx = 0;
            formPanel.add(label, gbc);
            gbc.gridx = 1;
            formPanel.add(field, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        };

        // Initialize Fields
        tfFirstName = new JTextField();
        tfMiddleName = new JTextField();
        tfLastName = new JTextField();
        tfBirth = new JTextField();
        tfAge = new JTextField();
        tfSex = new JTextField();
        tfCivilStatus = new JTextField();
        tfContact = new JTextField();
        tfHouse = new JTextField();
        tfOccupation = new JTextField();

        // Add Fields
        addField.accept("First Name", tfFirstName);
        addField.accept("Middle Name", tfMiddleName);
        addField.accept("Last Name", tfLastName);
        addField.accept("Date of Birth", tfBirth);
        addField.accept("Age", tfAge);
        addField.accept("Sex", tfSex);
        addField.accept("Civil Status", tfCivilStatus);
        addField.accept("Contact Number", tfContact);
        addField.accept("House Number", tfHouse);
        addField.accept("Occupation", tfOccupation);

        // Action Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnRow.setBackground(color1);

        btnSave = new JButton("Save Resident");
        btnSave.setFont(new Font("Arial", Font.BOLD, 14));
        btnSave.addActionListener(e -> saveResident());

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.addActionListener(e -> {
            cardLayout.show(cardContainer, "LIST");
        });

        btnRow.add(btnSave);
        btnRow.add(btnCancel);

        // Layout adjustment for buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(btnRow, gbc);
    }

    private void saveResident() {
        // Validate inputs (simple check)
        if (tfFirstName.getText().isEmpty() || tfLastName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First and Last Name are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Assign to fields (simulating your old logic)
        firstName = tfFirstName.getText();
        middleName = tfMiddleName.getText();
        lastName = tfLastName.getText();
        birth = tfBirth.getText();
        age = tfAge.getText();
        sex = tfSex.getText();
        civilStatus = tfCivilStatus.getText();
        contactNumber = tfContact.getText();
        houseNumber = tfHouse.getText();
        occupation = tfOccupation.getText();

        try (BufferedWriter RBIdata = new BufferedWriter(new FileWriter("src/RBI/RBIdata.dat", true))) {
            RBIdata.write("First Name:\t\t" + firstName + "\n");
            RBIdata.write("Middle Name:\t\t" + middleName + "\n");
            RBIdata.write("Last Name:\t\t" + lastName + "\n");
            RBIdata.write("Birth Date:\t\t" + birth + "\n");
            RBIdata.write("Age:\t\t" + age + "\n");
            RBIdata.write("Sex:\t\t" + sex + "\n");
            RBIdata.write("Civil Status:\t\t" + civilStatus + "\n");
            RBIdata.write("Contact Number:\t" + contactNumber + "\n");
            RBIdata.write("House Number:\t" + houseNumber + "\n");
            RBIdata.write("Occupation:\t\t" + occupation + "\n");
            RBIdata.write("--------------------------------------------------\n"); // Separator

            JOptionPane.showMessageDialog(this, "Resident saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Switch back to list
            cardLayout.show(cardContainer, "LIST");
            updateResidenceLists();
            clearForm();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfFirstName.setText("");
        tfMiddleName.setText("");
        tfLastName.setText("");
        tfBirth.setText("");
        tfAge.setText("");
        tfSex.setText("");
        tfCivilStatus.setText("");
        tfContact.setText("");
        tfHouse.setText("");
        tfOccupation.setText("");
    }

    public void updateResidenceLists() {
        residences.setText(""); // Clear current text first
        File file = new File("src/RBI/RBIdata.dat");

        if (!file.exists()) {
            residences.setText("No records found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                residences.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            residences.setText("Error: Could not find data file.");
        } catch (IOException e) {
            residences.setText("Error: Could not read data file.");
        }
    }
}