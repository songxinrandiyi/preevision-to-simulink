package preevisiontosimulink.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import preevisiontosimulink.parser.kblelements.GeneralWire;

public class KBLModifier {

	public static void generateModifiedKBL(List<File> kblFiles, List<GeneralWire> validGeneralWires) {

		File kblFile = kblFiles.get(0);
		File outputDir = kblFile.getParentFile();

		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(kblFile);
			modifyKBLInformation(document, validGeneralWires);

			File outputFile = FileUtils.generateOutputFile(kblFile, outputDir);

			// Save the modified document to the new file
			FileUtils.saveDocument(document, outputFile);

		} catch (JDOMException e) {
			System.out.println("XML is not valid against the schema.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void modifyKBLInformation(Document document, List<GeneralWire> validGeneralWires) {
		Element rootElement = document.getRootElement();
		List<Element> generalWires = rootElement.getChildren("General_wire");

		for (GeneralWire generalWire : validGeneralWires) {
			JDOMUtils.setCrossSectionArea(generalWires, generalWire.getId(),
					generalWire.getCrossSectionArea().getValueComponent());
			JDOMUtils.setGeneralWirePartNumber(generalWires, generalWire.getId(), generalWire.getPartNumber());
		}
	}
}
