package preevisiontosimulink.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;
import preevisiontosimulink.proxy.system.SubsystemType;

public class ExcelGenerator {

    public static void generateExcel(String modelName, SimulinkSystem system) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = createSheetWithHeaders(workbook, modelName, false);
        
        List<SimulinkSubsystem> subsystems = filterAndSortSubsystems(system.getSubsystemList(SubsystemType.KABEL));
        populateDataRows(sheet, subsystems, false);
        
        autoSizeColumns(sheet, 24);
        writeToFile(workbook, modelName + ".xlsx");
    }

    public static void generateUpdatedExcel(String modelName, SimulinkSystem system) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = createSheetWithHeaders(workbook, modelName, true);
        
        List<SimulinkSubsystem> subsystems = filterAndSortSubsystems(system.getSubsystemList(SubsystemType.KABEL));
        populateDataRows(sheet, subsystems, true);
        
        autoSizeColumns(sheet, 11);
        writeToFile(workbook, modelName + "_1.xlsx");
    }

    private static Sheet createSheetWithHeaders(Workbook workbook, String modelName, boolean isUpdated) {
        Sheet sheet = workbook.createSheet(modelName);
        Row headerRow = sheet.createRow(0);
        
        if (isUpdated) {
            String[] headers = {"Wire", "Typical current", "Max Peak Current", "Max Peak Current Duration",
                                "WireConnector1", "Pinnummer1", "WireConnector2", "Pinnummer2", "Schematic Pin", "Ø", 
                                "Wire Length", "Voltage Drop with 1A"};
            createHeaderCells(headerRow, headers);
        } else {
            String[] headers = {"Wire", "Component1", "WireConnector1", "Pinnummer1", "SchematicPin1", "PinType1", 
                                "SealType1", "Typical Current", "Max Peak Current", "Max Peak Current Duration", 
                                "Component2", "WireConnector2", "Pinnummer2", "Schematic Pin2", "PinType2", "SealType2", 
                                "Typical current", "Max Peak Current", "Max Peak Current Duration", "WireType", "Ø", 
                                "Wire Length", "FeatureNo.", "Feature", "GeometryDiagram"};
            createHeaderCells(headerRow, headers);
        }
        
        return sheet;
    }

    private static void createHeaderCells(Row headerRow, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private static List<SimulinkSubsystem> filterAndSortSubsystems(List<SimulinkSubsystem> subsystems) {
        List<SimulinkSubsystem> filteredSubsystems = new ArrayList<>();
        for (SimulinkSubsystem subsystem : subsystems) {
            if (subsystem.getContactPoints() != null && subsystem.getContactPoints().size() == 2) {
                filteredSubsystems.add(subsystem);
            }
        }
        
        // Sort the subsystems by the name of the first contact point, and if the names are equal, by pinNumberTo
        Collections.sort(filteredSubsystems, new Comparator<SimulinkSubsystem>() {
            @Override
            public int compare(SimulinkSubsystem subsystem1, SimulinkSubsystem subsystem2) {
                Contact contact1 = subsystem1.getContactPoints().get(0);
                Contact contact2 = subsystem2.getContactPoints().get(0);

                // First, compare by name
                int nameComparison = contact1.getName().compareTo(contact2.getName());
                
                if (nameComparison != 0) {
                    return nameComparison;
                }

                // If names are equal, compare by pinNumberTo
                return contact1.getPinNumberTo().compareTo(contact2.getPinNumberTo());
            }
        });

        return filteredSubsystems;
    }

    private static void populateDataRows(Sheet sheet, List<SimulinkSubsystem> subsystems, boolean isUpdated) {
        CellStyle leftAlignStyle = sheet.getWorkbook().createCellStyle();
        leftAlignStyle.setAlignment(HorizontalAlignment.LEFT);

        for (int i = 0; i < subsystems.size(); i++) {
            Row row = sheet.createRow(i + 1);
            SimulinkSubsystem subsystem = subsystems.get(i);
            if (isUpdated) {
                createUpdatedDataRow(row, subsystem, leftAlignStyle);
            } else {
                createDataRow(row, subsystem);
            }
        }
    }

    private static void createDataRow(Row row, SimulinkSubsystem subsystem) {
        if (subsystem.getKblInformation().getWireNumber() != null) {
            row.createCell(0).setCellValue(subsystem.getKblInformation().getWireNumber());
        }
        row.createCell(2).setCellValue(subsystem.getContactPoints().get(0).getName());
        row.createCell(3).setCellValue(subsystem.getContactPoints().get(0).getPinNumberTo());
        row.createCell(11).setCellValue(subsystem.getContactPoints().get(1).getName());
        row.createCell(12).setCellValue(subsystem.getContactPoints().get(1).getPinNumberTo());
        if (subsystem.getKblInformation().getCrossSectionArea() != null) {
            row.createCell(20).setCellValue(subsystem.getKblInformation().getCrossSectionArea());
            row.createCell(8).setCellValue(subsystem.getKblInformation().getCrossSectionArea() * 6);
            row.createCell(17).setCellValue(subsystem.getKblInformation().getCrossSectionArea() * 6);
        }
        if (subsystem.getKblInformation().getLength() != null) {
            row.createCell(21).setCellValue(subsystem.getKblInformation().getLength());
        }
        if (subsystem.getKblInformation().getSignalName() != null) {
            row.createCell(4).setCellValue(subsystem.getKblInformation().getSignalName());
            row.createCell(13).setCellValue(subsystem.getKblInformation().getSignalName());
        }
    }

    private static void createUpdatedDataRow(Row row, SimulinkSubsystem subsystem, CellStyle leftAlignStyle) {
        if (subsystem.getKblInformation().getWireNumber() != null) {
            row.createCell(0).setCellValue(subsystem.getKblInformation().getWireNumber());
        }
        row.createCell(4).setCellValue(subsystem.getContactPoints().get(0).getName());
        Cell cell5 = row.createCell(5);
        cell5.setCellValue(subsystem.getContactPoints().get(0).getPinNumberTo());
        cell5.setCellStyle(leftAlignStyle);
        row.createCell(6).setCellValue(subsystem.getContactPoints().get(1).getName());
        Cell cell7 = row.createCell(7);
        cell7.setCellStyle(leftAlignStyle);
        cell7.setCellValue(subsystem.getContactPoints().get(1).getPinNumberTo());
        if (subsystem.getKblInformation().getCrossSectionArea() != null) {
            row.createCell(9).setCellValue(subsystem.getKblInformation().getCrossSectionArea());
        }
        if (subsystem.getKblInformation().getLength() != null) {
            row.createCell(10).setCellValue(subsystem.getKblInformation().getLength());
        }
        if (subsystem.getKblInformation().getCrossSectionArea() != null && subsystem.getKblInformation().getLength() != null) {
            double voltageDrop = 1 * CalculatorUtils.calculateResistance(subsystem.getKblInformation().getLength(),
                    subsystem.getKblInformation().getCrossSectionArea());
            row.createCell(11).setCellValue(voltageDrop);
        }
        if (subsystem.getKblInformation().getSignalName() != null) {
            row.createCell(8).setCellValue(subsystem.getKblInformation().getSignalName());
        }
    }

    private static void autoSizeColumns(Sheet sheet, int numberOfColumns) {
        for (int i = 0; i <= numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static void writeToFile(Workbook workbook, String fileName) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
