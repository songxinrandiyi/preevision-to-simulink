package preevisiontosimulink.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlModifier {
    public static void main(String[] args) {
        String filePath = "ENGINE_COMPARTMENT_RIGHT_05292024_1455.kbl";
        String newValue = "800.0";
        modifyXmlFile(filePath, newValue);
    }

    public static void modifyXmlFile(String filePath, String newValue) {
        try {
            // Erstellen Sie einen SAXBuilder zum Parsen der XML-Datei
            SAXBuilder builder = new SAXBuilder();
            File xmlFile = new File(filePath);
            Document document = builder.build(xmlFile);

            // Finden Sie das zu ändernde Element
            Element root = document.getRootElement();
            Element harness = root.getChild("Harness");
            List<Element> generalWireOccurrences = harness.getChildren("General_wire_occurrence");
            
            // Wählen Sie das zweite General_wire_occurrence-Element aus
            if (generalWireOccurrences.size() >= 2) {
                Element selectedGeneralWireOccurrence = generalWireOccurrences.get(1); // Index 1 für das zweite Element
                Element lengthInformation = selectedGeneralWireOccurrence.getChild("Length_information").getChild("Length_value");

                // Ändern Sie den Wert des Elements
                lengthInformation.setAttribute("Value_component", newValue);

                // Schreiben Sie die modifizierte XML-Datei
                XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                try (FileOutputStream fileOut = new FileOutputStream(xmlFile)) {
                    outputter.output(document, fileOut);
                }
                System.out.println("Datei erfolgreich modifiziert.");
            } else {
                System.out.println("Es gibt weniger als zwei General_wire_occurrence-Elemente.");
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
