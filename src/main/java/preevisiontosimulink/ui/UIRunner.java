package preevisiontosimulink.ui;

import preevisiontosimulink.generator.ModelGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIRunner {
    private static JTextField modelNameField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulink Model Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Set the location of the main frame to the center of the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Simulink Model Generator");
        titleLabel.setBounds(120, 20, 160, 25);
        panel.add(titleLabel);

        JLabel modelNameLabel = new JLabel("Model Name:");
        modelNameLabel.setBounds(50, 60, 80, 25);
        panel.add(modelNameLabel);

        modelNameField = new JTextField(20);
        modelNameField.setBounds(140, 60, 200, 25);
        panel.add(modelNameField);

        JButton runButton = new JButton("Generate");
        runButton.setBounds(150, 100, 100, 25);
        panel.add(runButton);

        JLabel statusLabel = new JLabel("");
        statusLabel.setBounds(120, 140, 160, 25);
        panel.add(statusLabel);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = modelNameField.getText();
                statusLabel.setText("Generating Simulink model...");
                ModelGenerator.generateModel(modelName);
                statusLabel.setText("Simulink model generated.");
            }
        });
    }
}
