package preevisiontosimulink.generator;

import javax.swing.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import preevisiontosimulink.parser.kblelements.*;

import java.io.File;
import java.util.List;

public class KBLUnmarshaller2 {

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

                JAXBContext jaxbContext = JAXBContext.newInstance(KBLContainer.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                JAXBElement<KBLContainer> rootElement = jaxbUnmarshaller.unmarshal(new StreamSource(selectedFile),
                        KBLContainer.class);
                KBLContainer kblContainer = rootElement.getValue();

                Harness harness = kblContainer.getHarness();
                List<GeneralWireOccurrence> generalWireOccurrences = harness.getGeneralWireOccurrences();
                GeneralWireOccurrence generalWireOccurrence = generalWireOccurrences.get(0);
                LengthInformation lengthInformation = generalWireOccurrence.getLengthInformation().get(0);
                lengthInformation.getLengthValue().setValueComponent(800.0);

                // Generate output file name with an incremented integer suffix
                File outputFile = generateOutputFile(selectedFile, outputDir);

                jaxbMarshaller.marshal(kblContainer, outputFile);
                System.out.println("Modified file saved to: " + outputFile.getAbsolutePath());
            } else {
                System.out.println("No file selected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private static CartesianPoint findCartesianPoint(List<CartesianPoint> cartesianPoints, String id) {
        for (CartesianPoint cartesianPoint : cartesianPoints) {
            if (cartesianPoint.getId().equals(id)) {
                return cartesianPoint;
            }
        }
        return null; // If no corresponding CartesianPoint is found
    }
}
