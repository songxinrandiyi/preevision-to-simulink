package preevisiontosimulink.parser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import preevisiontosimulink.library.LConnection;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkExternRelation;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiringHarnessFromExcel {
	private SimulinkSystem system;
	private String modelName;
	private List<String> kblFilePaths = new ArrayList<>();
	
    public WiringHarnessFromExcel(String modelName, List<String> kblFilePaths) {
        this.modelName = modelName;
        this.kblFilePaths = kblFilePaths;
    }
	
    public void generateModel() {
        system = new SimulinkSystem(modelName);
        for (String path : kblFilePaths) {
            try (FileInputStream fis = new FileInputStream(path);
                    Workbook workbook = new XSSFWorkbook(fis)) {

                   for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                       Sheet sheet = workbook.getSheetAt(i);
                       System.out.println("Sheet: " + sheet.getSheetName());
                       generateBlocks(sheet);
                       generateConnections(sheet);
                       generateReplacement();
                   }

               } catch (IOException e) {
                   e.printStackTrace();
               }
        }      
        system.generateModel();
    }

    private void generateBlocks(Sheet sheet) {
        int rowBegin = 1; // Start from the second row
        for (int i = rowBegin; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue; // Skip if row is null
            }
            
            Cell component1 = row.getCell(1);
            Cell pinnummer1 = row.getCell(3);
            Cell schematicPin1 = row.getCell(4);
			String pin1Name = replaceSlashWithUnderscore(pinnummer1.getStringCellValue() + "_" + schematicPin1.getStringCellValue());
			String component1Name = component1.getStringCellValue();
			boolean hasComponent1 = !component1Name.isEmpty() && !pinnummer1.getStringCellValue().isEmpty() && !schematicPin1.getStringCellValue().isEmpty();
			
            Cell component2 = row.getCell(10);
            Cell pinnummer2 = row.getCell(12);
            Cell schematicPin2 = row.getCell(13);         
            String pin2Name = replaceSlashWithUnderscore(pinnummer2.getStringCellValue() + "_" + schematicPin2.getStringCellValue());
            String component2Name = component2.getStringCellValue();
            boolean hasComponent2 = !component2Name.isEmpty() && !pinnummer2.getStringCellValue().isEmpty() && !schematicPin2.getStringCellValue().isEmpty();
            
            if (hasComponent1 && hasComponent2) {
                if (system.getSubsystem(component1Name) == null) {
                    system.addSubsystem(new SimulinkSubsystem(system, component1Name));
                }
                SimulinkSubsystem subsystem1 = system.getSubsystem(component1Name);
                if (subsystem1.getInConnection(pin1Name) == null) {
                    subsystem1.addInConnection(new LConnection(subsystem1, pin1Name));
					subsystem1.reorderConnections();
                }
                
                if (system.getSubsystem(component2Name) == null) {
                    system.addSubsystem(new SimulinkSubsystem(system, component2Name));
                }
                SimulinkSubsystem subsystem2 = system.getSubsystem(component2Name);
                if (subsystem2.getInConnection(pin2Name) == null) {
                    subsystem2.addInConnection(new LConnection(subsystem2, pin2Name));
                    subsystem2.reorderConnections();
                }                         
            }
        }
    }
    
    private void generateConnections(Sheet sheet) {
        int rowBegin = 1; // Start from the second row
        for (int i = rowBegin; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue; // Skip if row is null
            }
            
            Cell component1 = row.getCell(1);
            Cell pinnummer1 = row.getCell(3);
            Cell schematicPin1 = row.getCell(4);
			String pin1Name = replaceSlashWithUnderscore(pinnummer1.getStringCellValue() + "_" + schematicPin1.getStringCellValue());
			String component1Name = component1.getStringCellValue();
			boolean hasComponent1 = !component1Name.isEmpty() && !pinnummer1.getStringCellValue().isEmpty() && !schematicPin1.getStringCellValue().isEmpty();
			
            Cell component2 = row.getCell(10);
            Cell pinnummer2 = row.getCell(12);
            Cell schematicPin2 = row.getCell(13);         
            String pin2Name = replaceSlashWithUnderscore(pinnummer2.getStringCellValue() + "_" + schematicPin2.getStringCellValue());
            String component2Name = component2.getStringCellValue();
            boolean hasComponent2 = !component2Name.isEmpty() && !pinnummer2.getStringCellValue().isEmpty() && !schematicPin2.getStringCellValue().isEmpty();
            
            if (hasComponent1 && hasComponent2) {          
                
				String name = component1Name + "  " + pin1Name + "  " + component2Name + "  " + pin2Name;
				system.addBlock(new Resistor(system, name));
				double resistance = calculateResistance(-convertStringToDouble(row.getCell(21).getStringCellValue()), 
						convertStringToDouble(row.getCell(20).getStringCellValue()));
				system.getBlock(name).setParameter("R", resistance);
				String pin1Path = system.getSubsystem(component1Name).getConnectionPath(pin1Name);
				system.addRelation(new SimulinkExternRelation(system.getBlock(name).getInPort(0), component1Name, pin1Path, system, 0));
				String pin2Path = system.getSubsystem(component2Name).getConnectionPath(pin2Name);
				system.addRelation(new SimulinkExternRelation(system.getBlock(name).getOutPort(0), component2Name, pin2Path, system, 0));
                
            }
        }
    }
    
    private void generateReplacement() {
		List <SimulinkSubsystem> subsystems = system.getSubsystemList();
		for (SimulinkSubsystem subsystem : subsystems) {
			List <LConnection> inPorts = subsystem.getInConnections();
			if (inPorts != null) {
				for (LConnection inPort : inPorts) {
					subsystem.addBlock(new Resistor(subsystem, inPort.getName() + "_R"));
					subsystem.getBlock(inPort.getName() + "_R").setParameter("R", 3);
					subsystem.addRelation(new SimulinkRelation(inPort.getInPort(0), subsystem.getBlock(inPort.getName() + "_R").getInPort(0), subsystem));
				}
				if (inPorts.size() > 1) {
					for (int i = 0; i < inPorts.size() - 1; i++) {
						LConnection endPort = inPorts.get(inPorts.size() - 1);
						LConnection startPort = inPorts.get(i);
						ISimulinkBlock endResistor = subsystem.getBlock(endPort.getName() + "_R");
						ISimulinkBlock startResistor = subsystem.getBlock(startPort.getName() + "_R");
						subsystem.addRelation(new SimulinkRelation(endResistor.getOutPort(0), startResistor.getOutPort(0), subsystem));
					}
				}
			}
		}
    }
    
    private String replaceSlashWithUnderscore(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("/", "_");
    }
    
    private double convertStringToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            System.out.println("Invalid string format for a double: " + str);
            return Double.NaN; // Return a default value
        }
    }
    
    private int convertStringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Invalid string format for an integer: " + str);
            return 0; // Return a default value, e.g., 0
        }
    }

    private void printCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                System.out.print(cell.getStringCellValue() + "\t");
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue() + "\t");
                } else {
                    System.out.print(cell.getNumericCellValue() + "\t");
                }
                break;
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t");
                break;
            default:
                System.out.print(" \t");
                break;
        }
    }
    
	private double calculateResistance(double length, double crossSectionalArea) {
        // Convert cross-sectional area from mm² to m² (1 mm² = 1e-6 m²)
        double crossSectionalAreaM2 = crossSectionalArea * 1e-6;

        // Calculate and return resistance using the formula R = ρ * (L / A)
        return 1.77e-8 * (length / crossSectionalAreaM2);
    }
    
    private static double extractMiddleDouble(String str) {
        // Split the string based on underscores
        String[] parts = str.split("_");
        
        // Check if the middle part exists
        if (parts.length < 3) {
            throw new IllegalArgumentException("String does not have a middle part: " + str);
        }

        // The middle part is at index 1
        String middlePart = parts[1];

        // Convert the middle part to a double
        try {
            return Double.parseDouble(middlePart);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid format for a double in the middle part: " + middlePart);
        }
    }
}

