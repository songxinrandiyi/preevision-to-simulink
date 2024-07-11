package preevisiontosimulink.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class FileUtils {
	public static File generateOutputFile(File inputFile, File outputDir) {
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

	public static void saveDocument(Document document, File outputFile) throws IOException {
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.setFormat(Format.getPrettyFormat());
		try (FileWriter writer = new FileWriter(outputFile)) {
			xmlOutputter.output(document, writer);
		}
	}
}
