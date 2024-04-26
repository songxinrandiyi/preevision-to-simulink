package preevisiontosimulink.ui;

import preevisiontosimulink.generator.SimulinkModelGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIRunner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulink Model Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Simulink Model Generator");
        titleLabel.setBounds(70, 20, 160, 25);
        panel.add(titleLabel);

        JButton runButton = new JButton("Run");
        runButton.setBounds(100, 70, 100, 25);
        panel.add(runButton);

        JLabel statusLabel = new JLabel("");
        statusLabel.setBounds(70, 110, 160, 25);
        panel.add(statusLabel);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Generating Simulink model...");
                SimulinkModelGenerator generator = new SimulinkModelGenerator();
                generator.generateModel();
                statusLabel.setText("Simulink model generated.");
            }
        });
    }
}