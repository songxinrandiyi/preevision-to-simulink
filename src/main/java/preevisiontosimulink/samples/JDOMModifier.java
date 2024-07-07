package preevisiontosimulink.samples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class JDOMModifier {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Create a file chooser dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".")); // Start in current directory
            int result = fileChooser.showOpenDialog(null); // Show open dialog

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                File outputDir = selectedFile.getParentFile(); // Get parent directory of selected file

                // Load the XML file into a JDOM Document
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = saxBuilder.build(selectedFile);

                // Perform modification on the XML document
                modifyLengthInformation(document);

                // Generate output file name with an incremented integer suffix
                File outputFile = generateOutputFile(selectedFile, outputDir);

                // Save the modified document to the new file
                saveDocument(document, outputFile);

                System.out.println("Modified file saved to: " + outputFile.getAbsolutePath());
            } else {
                System.out.println("No file selected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void modifyLengthInformation(Document document) {
        Element rootElement = document.getRootElement();
        List<Element> generalWires = rootElement.getChildren("General_wire");
        for (Element generalWire : generalWires) {
            if (generalWire.getAttributeValue("id").equals("Na660fb818f283ad762245e6XNa660fb818f283ad762245e500")) {
                Element crossSectionArea = generalWire.getChild("Cross_section_area");
                if (crossSectionArea != null) {
                    Element valueComponent = crossSectionArea.getChild("Value_component");
                    if (valueComponent != null) {
                        valueComponent.setText("0.1");
                    }
                }
            }
        }
        
        Element harness = rootElement.getChild("Harness");
        if (harness != null) {
            List<Element> generalWireOccurrences = harness.getChildren("General_wire_occurrence");
            if (!generalWireOccurrences.isEmpty()) {
            	for (Element generalWireOccurrence : generalWireOccurrences) {
            		if (generalWireOccurrence.getAttributeValue("id").equals("Na66117818b3c0e004dc5c71XNa66117818b3c0e004dc5c6b00")) {
                        List<Element> lengthInformationList = generalWireOccurrence.getChildren("Length_information");
                        if (!lengthInformationList.isEmpty()) {
                            Element lengthInformation = lengthInformationList.get(0);
                            Element lengthValue = lengthInformation.getChild("Length_value");
                            if (lengthValue != null) {
                                Element valueComponent = lengthValue.getChild("Value_component");
                                if (valueComponent != null) {
                                    valueComponent.setText("800.0");
                                }
                            }
                        }
            		}
            	}
            }
        }
    }

    private static File generateOutputFile(File inputFile, File outputDir) {
        String fileName = inputFile.getName();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        int count = 1;
        File outputFile = new File(outputDir, baseName + "_" + count + extension);
        while (outputFile.exists()) {
            count++;
            outputFile = new File(outputDir, baseName + "_" + count + extension);
        }
        return outputFile;
    }

    private static void saveDocument(Document document, File outputFile) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());
        try (FileWriter writer = new FileWriter(outputFile)) {
            xmlOutputter.output(document, writer);
        }
    }
}
