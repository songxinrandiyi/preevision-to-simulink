package preevisiontosimulink.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;
import preevisiontosimulink.proxy.system.SubsystemType;

public class KBLModifier {
    
    public static void generateModifiedKBL(SimulinkSystem system, List<File> xlsxFiles, List<File> kblFiles) {
        for (File xlsxFile : xlsxFiles) {
            try (FileInputStream fis = new FileInputStream(xlsxFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    System.out.println("Sheet: " + sheet.getSheetName());
                    getInformationFromModifiedExcel(sheet, system);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<SimulinkSubsystem> subsystems = system.getSubsystemList(SubsystemType.KABEL);

        List<SimulinkSubsystem> filteredSubsystems = new ArrayList<>();

        for (SimulinkSubsystem subsystem : subsystems) {
            if (subsystem.getContactPoints() != null && subsystem.getContactPoints().size() == 2
                    && subsystem.getKblInformation().getLength() != null && subsystem.getKblInformation().getCrossSectionArea() != null
                    && subsystem.getKblInformation().getGeneralWireId() != null && subsystem.getKblInformation().getGeneralWireOccurrenceId() != null) {
                filteredSubsystems.add(subsystem);
            }
        }

        File kblFile = kblFiles.get(0);
        File outputDir = kblFile.getParentFile();

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(kblFile);
            modifyKBLInformation(document, filteredSubsystems);

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

    private static void modifyKBLInformation(Document document, List<SimulinkSubsystem> filteredSubsystems) {
        Element rootElement = document.getRootElement();
        Element harness = rootElement.getChild("Harness");
        List<Element> generalWires = rootElement.getChildren("General_wire");
        List<Element> generalWireOccurrences = harness.getChildren("General_wire_occurrence");

        if (generalWires != null && generalWireOccurrences != null && harness != null) {
            for (SimulinkSubsystem simulinkSubsystem : filteredSubsystems) {
                JDOMUtils.setCrossSectionArea(generalWires, simulinkSubsystem.getKblInformation().getGeneralWireId(), simulinkSubsystem.getKblInformation().getCrossSectionArea());
                JDOMUtils.setLength(generalWireOccurrences, simulinkSubsystem.getKblInformation().getGeneralWireOccurrenceId(), simulinkSubsystem.getKblInformation().getLength());
            }
        }
    }

    private static void getInformationFromModifiedExcel(Sheet sheet, SimulinkSystem system) {
        int rowBegin = 1; // Start from the second row
        for (int i = rowBegin; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if (row == null) {
                continue; // Skip if row is null
            }

            Cell connector1Cell = row.getCell(4);
            Cell pin1Cell = row.getCell(5);
            Cell connector2Cell = row.getCell(6);
            Cell pin2Cell = row.getCell(7);

            String connector1 = null;
            String connector2 = null;
            Integer pin1 = null;
            Integer pin2 = null;

            Cell crossSectionAreaCell = row.getCell(9);
            Cell wireLengthCell = row.getCell(10);

            Double crossSectionArea = null;
            Double wireLength = null;

            Boolean notNull = false;
            Boolean notBlank = false;

            notNull = crossSectionAreaCell != null && wireLengthCell != null && connector1Cell != null
                    && pin1Cell != null && connector2Cell != null && pin2Cell != null;
            if (notNull) {
                notBlank = crossSectionAreaCell.getCellType() != CellType.BLANK && wireLengthCell.getCellType() != CellType.BLANK
                        && connector1Cell.getCellType() != CellType.BLANK && pin1Cell.getCellType() != CellType.BLANK
                        && connector2Cell.getCellType() != CellType.BLANK && pin2Cell.getCellType() != CellType.BLANK;
            }

            if (notBlank) {
                connector1 = connector1Cell.getStringCellValue();
                pin1 = CellUtils.getIntegerValueFromCell(pin1Cell);
                if (pin1 == null) {
                    pin1 = 1;
                }

                Contact leftContact = new Contact(connector1, pin1, 1);

                connector2 = connector2Cell.getStringCellValue();
                pin2 = CellUtils.getIntegerValueFromCell(pin2Cell);
                if (pin2 == null) {
                    pin2 = 1;
                }

                Contact rightContact = new Contact(connector2, pin2, 2);

                SimulinkSubsystem subsystem = system.findSubsystemWithContactPoints(leftContact, rightContact);

                crossSectionArea = CellUtils.getNumericValueFromCell(crossSectionAreaCell);
                wireLength = CellUtils.getNumericValueFromCell(wireLengthCell);

                if (subsystem != null && crossSectionArea != null && wireLength != null
                        && subsystem.getKblInformation().getCrossSectionArea() != null
                        && subsystem.getKblInformation().getLength() != null) {
                    if (!crossSectionArea.equals(subsystem.getKblInformation().getCrossSectionArea())) {
                        subsystem.getKblInformation().setCrossSectionArea(crossSectionArea);
                    }
                    if (!wireLength.equals(subsystem.getKblInformation().getLength())) {
                        subsystem.getKblInformation().setLength(wireLength);
                    }
                }
            }

            System.out.println();
        }
    }
}
