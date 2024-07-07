package preevisiontosimulink.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import preevisiontosimulink.parser.KBLParser;
import preevisiontosimulink.proxy.system.SimulinkSystem;
import preevisiontosimulink.util.StringUtils;

public class MainWindow {
    private static JTextField modelNameField;
    private static JLabel fileLabel;
    private static List<File> selectedFiles = new ArrayList<>();
    private static JComboBox<String> operationComboBox;
    private static JButton generateButton;
    private static JButton clearFilesButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulink Model Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 350); // Increase the size to ensure components fit well

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // Set the location of the main frame to the center of the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel modelNameLabel = new JLabel("Model Name:");
        modelNameLabel.setBounds(50, 60, 80, 25);
        panel.add(modelNameLabel);

        modelNameField = new JTextField(20);
        modelNameField.setBounds(140, 60, 300, 25);
        panel.add(modelNameField);

        JLabel fileChooserLabel = new JLabel("Select Files:");
        fileChooserLabel.setBounds(50, 100, 80, 25);
        panel.add(fileChooserLabel);

        JButton fileButton = new JButton("Browse");
        fileButton.setBounds(140, 100, 100, 25);
        panel.add(fileButton);

        clearFilesButton = new JButton("Clear Files"); // New button
        clearFilesButton.setBounds(250, 100, 100, 25); // Positioning the button directly to the right of "Browse" button
        panel.add(clearFilesButton);

        fileLabel = new JLabel("");
        fileLabel.setBounds(360, 100, 200, 25);
        panel.add(fileLabel);

        JLabel operationLabel = new JLabel("Operation:");
        operationLabel.setBounds(50, 140, 80, 25);
        panel.add(operationLabel);

        String[] operations = {"Generate Wiring Harness", "Generate Excel", "Generate Modified KBL"};
        operationComboBox = new JComboBox<>(operations);
        operationComboBox.setBounds(140, 140, 300, 25);
        panel.add(operationComboBox);

        generateButton = new JButton("Generate");
        generateButton.setBounds(140, 180, 150, 25);
        panel.add(generateButton);
        generateButton.setEnabled(false);

        JLabel statusLabel = new JLabel("");
        statusLabel.setBounds(160, 220, 200, 25); // Adjust Y position to avoid overlapping with other components
        panel.add(statusLabel);

        // Listener to enable buttons based on input validation
        SimpleDocumentListener validationListener = new SimpleDocumentListener() {
            @Override
            public void update() {
                checkEnableGenerateButtons();
            }
        };
        modelNameField.getDocument().addDocumentListener(validationListener);

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); // Set the current directory
                fileChooser.setMultiSelectionEnabled(true); // Enable multiple selection

                int result = fileChooser.showOpenDialog(panel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    StringBuilder fileNames = new StringBuilder(fileLabel.getText());
                    for (File file : files) {
                        if (!selectedFiles.contains(file)) {
                            selectedFiles.add(file);
                            fileNames.append(file.getName()).append("; ");
                        }
                    }
                    fileLabel.setText(fileNames.toString());
                    fileLabel.setToolTipText(fileNames.toString()); // Show the file names on hover
                    checkEnableGenerateButtons();
                }
            }
        });

        clearFilesButton.addActionListener(new ActionListener() { // Action listener for clear button
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFiles.clear();
                fileLabel.setText("");
                checkEnableGenerateButtons();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modelName = modelNameField.getText();
                if (modelName.isEmpty() && !selectedFiles.isEmpty()) {
                    // Get first part of the name of the first two selected KBL files
                    List<String> fileNameParts = new ArrayList<>();
                    String fileName = null;
                    for (File file : selectedFiles) {
                        if (file.getName().endsWith(".kbl")) {
                            fileNameParts.add(StringUtils.getFirstPart(file.getName()));
                            fileName = StringUtils.removeEnding(file.getName());
                            if (fileNameParts.size() == 2) {
                                break; // We only need the first two KBL files
                            }
                        }
                    }
                    if (fileNameParts.size() == 2) {
                        modelName = fileNameParts.get(0) + "_" + fileNameParts.get(1);
                    } else {
                        modelName = fileName;
                    }
                }

                if (modelName.isEmpty() || selectedFiles.isEmpty()) {
                    statusLabel.setText("Model name or files not selected.");
                    return;
                }

                String selectedOperation = (String) operationComboBox.getSelectedItem();
                if (selectedOperation == null) {
                    statusLabel.setText("No operation selected.");
                    return;
                }

                List<String> filePaths = new ArrayList<>();
                for (File file : selectedFiles) {
                    filePaths.add(file.getAbsolutePath());
                }

                SimulinkSystem system = new SimulinkSystem(modelName);

                switch (selectedOperation) {
                    case "Generate Wiring Harness":
                        statusLabel.setText("Generating Simulink model...");
                        KBLParser wiringHarness = new KBLParser(modelName, filePaths);
                        wiringHarness.generateModel();
                        statusLabel.setText("Simulink model generated.");
                        break;
                    case "Generate Excel":
                        statusLabel.setText("Generating Simulink model...");
                        KBLParser excel = new KBLParser(modelName, filePaths);
                        excel.generateExcel();
                        excel.generateUpdatedExcel();
                        statusLabel.setText("Excel table generated.");
                        break;
                    case "Generate Modified KBL":
                        statusLabel.setText("Generating Modified KBL...");
                        KBLParser modifiedKBL = new KBLParser(modelName, filePaths);
                        modifiedKBL.generateModifiedKBL();
                        statusLabel.setText("Modified KBL generated.");
                        break;
                    default:
                        statusLabel.setText("Unknown operation selected.");
                        return;
                }

                // Use a Timer to clear the status label after 2 seconds
                new javax.swing.Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        statusLabel.setText("");
                    }
                }).start();
            }
        });
    }

    private static void checkEnableGenerateButtons() {
        boolean hasKBLFile = selectedFiles.stream().anyMatch(file -> file.getName().endsWith(".kbl"));
        generateButton.setEnabled(hasKBLFile);
    }
}

abstract class SimpleDocumentListener implements javax.swing.event.DocumentListener {
    public abstract void update();

    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        update();
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        update();
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        update();
    }
}
