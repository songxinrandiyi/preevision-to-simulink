package preevisiontosimulink.parser;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import preevisiontosimulink.parser.kblelements.*;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkExternRelation;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.relation.SimulinkSubToSubRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;
import preevisiontosimulink.proxy.system.SubsystemType;
import preevisiontosimulink.util.CalculatorUtils;
import preevisiontosimulink.util.CellUtils;
import preevisiontosimulink.util.KBLUtils;
import preevisiontosimulink.util.StringUtil;
import preevisiontosimulink.proxy.port.Contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WiringHarnessFromKBL {
	private SimulinkSystem system;
	private String modelName;
	private List<File> kblFiles = new ArrayList<>();
	private List<File> xlsxFiles = new ArrayList<>();
	private Double currentValue = null;

	private KBLContainer kblContainer;
	private List<ConnectorHousing> connectorHousings = new ArrayList<>();
	private List<Node> nodes = new ArrayList<>();
	private List<GeneralWire> generalWires = new ArrayList<>();
	private List<Harness> harnesses = new ArrayList<>();
	private List<Segment> segments = new ArrayList<>();
	private List<Unit> units = new ArrayList<>();
	private List<Connection> connections = new ArrayList<>();
	private List<ConnectorOccurrence> connectorOccurrences = new ArrayList<>();
	private List<GeneralWireOccurrence> generalWireOccurrences = new ArrayList<>();

	public WiringHarnessFromKBL(String modelName, List<String> paths, Double currentValue) {
		this.modelName = modelName;
		for (String path : paths) {
			File file = new File(path);
			if (path.endsWith(".kbl")) {
				kblFiles.add(file);
			} else if (path.endsWith(".xlsx")) {
				xlsxFiles.add(file);
			}
		}
		this.currentValue = currentValue;
		init();
	}

	public void generateModel() {
		system = new SimulinkSystem(modelName);

		generateBlocksAndConnections();
	
		for (File xlsxFile : xlsxFiles) {
		    try (FileInputStream fis = new FileInputStream(xlsxFile); 
		         Workbook workbook = new XSSFWorkbook(fis)) {

		        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
		            Sheet sheet = workbook.getSheetAt(i);
		            System.out.println("Sheet: " + sheet.getSheetName());
		            getInformationFromExcel(sheet);
		        }

		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}	
		
		system.generateModel();
	}
	
	public void generateExcel() {
	    system = new SimulinkSystem(modelName);

	    generateBlocksAndConnections();

	    // Create a Workbook
	    Workbook workbook = new XSSFWorkbook(); // .xlsx format
	    // Workbook workbook = new HSSFWorkbook(); // .xls format

	    // Create a Sheet
	    Sheet sheet = workbook.createSheet(modelName);

	    // Create a Row for header
	    Row headerRow = sheet.createRow(0);

	    // Create header cells
	    headerRow.createCell(0).setCellValue("Wire");
	    headerRow.createCell(1).setCellValue("Component1");
	    headerRow.createCell(2).setCellValue("WireConnector1");
	    headerRow.createCell(3).setCellValue("Pinnummer1");
	    headerRow.createCell(4).setCellValue("SchematicPin1");
	    headerRow.createCell(5).setCellValue("PinType1");
	    headerRow.createCell(6).setCellValue("SealType1");
	    headerRow.createCell(7).setCellValue("Typical Current");
	    headerRow.createCell(8).setCellValue("Max Peak Current");
	    headerRow.createCell(9).setCellValue("Max Peak Current Duration");
	    headerRow.createCell(10).setCellValue("Component2");
	    headerRow.createCell(11).setCellValue("WireConnector2");
	    headerRow.createCell(12).setCellValue("Pinnummer2");
	    headerRow.createCell(13).setCellValue("Schematic Pin2");
	    headerRow.createCell(14).setCellValue("PinType2");
	    headerRow.createCell(15).setCellValue("SealType2");
	    headerRow.createCell(16).setCellValue("Typical current");
	    headerRow.createCell(17).setCellValue("Max Peak Current");
	    headerRow.createCell(18).setCellValue("Max Peak Current Duration");
	    headerRow.createCell(19).setCellValue("WireType");
	    headerRow.createCell(20).setCellValue("Ø");
	    headerRow.createCell(21).setCellValue("Wire Length");
	    headerRow.createCell(22).setCellValue("FeatureNo.");
	    headerRow.createCell(23).setCellValue("Feature");
	    headerRow.createCell(24).setCellValue("GeometryDiagram");

	    List<SimulinkSubsystem> subsystems = system.getSubsystemList(SubsystemType.KABEL);
	    
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

	    // Create data rows
	    for (int i = 1; i <= filteredSubsystems.size(); i++) {
	        Row row = sheet.createRow(i);

	        SimulinkSubsystem subsystem = filteredSubsystems.get(i - 1);
	        
	        if (subsystem.getWireNumber() != null) {
	        	row.createCell(0).setCellValue(subsystem.getWireNumber());
	        }
	        row.createCell(2).setCellValue(subsystem.getContactPoints().get(0).getName());
	        row.createCell(3).setCellValue(subsystem.getContactPoints().get(0).getPinNumberTo());
            row.createCell(11).setCellValue(subsystem.getContactPoints().get(1).getName());
            row.createCell(12).setCellValue(subsystem.getContactPoints().get(1).getPinNumberTo());
            if (subsystem.getCrossSectionArea() != null) {
            	row.createCell(20).setCellValue(subsystem.getCrossSectionArea());
            	row.createCell(8).setCellValue(subsystem.getCrossSectionArea()*6);
            	row.createCell(17).setCellValue(subsystem.getCrossSectionArea()*6);
            }
            if (subsystem.getLength() != null) {
            	row.createCell(21).setCellValue(subsystem.getLength());
            }
            if (subsystem.getSignalName() != null) {
            	row.createCell(4).setCellValue(subsystem.getSignalName());
            	row.createCell(13).setCellValue(subsystem.getSignalName());
            }
	    }

	    // Resize columns to fit the content
	    for (int i = 0; i <= 24; i++) {
	        sheet.autoSizeColumn(i);
	    }

	    // Write the output to a file
	    try (FileOutputStream fileOut = new FileOutputStream(modelName + ".xlsx")) {
	        workbook.write(fileOut);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        // Closing the workbook
	        try {
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void generateUpdatedExcel() {
		
	    // Create a Workbook
	    Workbook workbook = new XSSFWorkbook(); // .xlsx format
	    // Workbook workbook = new HSSFWorkbook(); // .xls format

	    // Create a Sheet
	    Sheet sheet = workbook.createSheet(modelName);

	    // Create a Row for header
	    Row headerRow = sheet.createRow(0);
	    
	    // Create a cell style with left alignment
	    CellStyle leftAlignStyle = workbook.createCellStyle();
	    leftAlignStyle.setAlignment(HorizontalAlignment.LEFT);

	    // Create header cells
	    headerRow.createCell(0).setCellValue("Wire");
	    headerRow.createCell(1).setCellValue("Typical current");
	    headerRow.createCell(2).setCellValue("Max Peak Current");
	    headerRow.createCell(3).setCellValue("Max Peak Current Duration");
	    headerRow.createCell(4).setCellValue("WireConnector1");
	    headerRow.createCell(5).setCellValue("Pinnummer1");
	    headerRow.createCell(6).setCellValue("WireConnector2");
	    headerRow.createCell(7).setCellValue("Pinnummer2");
	    headerRow.createCell(8).setCellValue("Schematic Pin");
	    headerRow.createCell(9).setCellValue("Ø");
	    headerRow.createCell(10).setCellValue("Wire Length");
	    headerRow.createCell(11).setCellValue("Voltage Drop with 1A");

	    List<SimulinkSubsystem> subsystems = system.getSubsystemList(SubsystemType.KABEL);
	    
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

	    // Create data rows
	    for (int i = 1; i <= filteredSubsystems.size(); i++) {
	        Row row = sheet.createRow(i);

	        SimulinkSubsystem subsystem = filteredSubsystems.get(i - 1);
	        
	        if (subsystem.getWireNumber() != null) {
	        	row.createCell(0).setCellValue(subsystem.getWireNumber());
	        }
	        row.createCell(4).setCellValue(subsystem.getContactPoints().get(0).getName());
	        Cell cell5 = row.createCell(5);
	        cell5.setCellValue(subsystem.getContactPoints().get(0).getPinNumberTo());
	        cell5.setCellStyle(leftAlignStyle);
            row.createCell(6).setCellValue(subsystem.getContactPoints().get(1).getName());
            Cell cell7 = row.createCell(7);
            cell7.setCellStyle(leftAlignStyle);
            cell7.setCellValue(subsystem.getContactPoints().get(1).getPinNumberTo());
            if (subsystem.getCrossSectionArea() != null) {
            	row.createCell(9).setCellValue(subsystem.getCrossSectionArea());
            }
            if (subsystem.getLength() != null) {
            	row.createCell(10).setCellValue(subsystem.getLength());
            }
            if (subsystem.getCrossSectionArea() != null && subsystem.getLength()!= null) {
            	double voltageDrop = 1*CalculatorUtils.calculateResistance(subsystem.getLength(), subsystem.getCrossSectionArea());
            	row.createCell(11).setCellValue(voltageDrop);
            }
            if (subsystem.getSignalName() != null) {
            	row.createCell(8).setCellValue(subsystem.getSignalName());
            }
	    }

	    // Resize columns to fit the content
	    for (int i = 0; i <= 11; i++) {
	        sheet.autoSizeColumn(i);
	    }

	    // Write the output to a file
	    try (FileOutputStream fileOut = new FileOutputStream(modelName + "_1.xlsx")) {
	        workbook.write(fileOut);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        // Closing the workbook
	        try {
	            workbook.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void generateModifiedKBL(Sheet sheet) {
		int rowBegin = 1; // Start from the second row
		for (int i = rowBegin; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			if (row == null) {
				continue; // Skip if row is null
			}
			Cell connector1 = row.getCell(2);
	        Cell pinnummer1 = row.getCell(3);
	        Cell connector2 = row.getCell(11);
	        Cell pinnummer2 = row.getCell(12);
	        Cell currentValue = row.getCell(7);
	        
	        String connectorName1 = null;
	        String connectorName2 = null;
	        Integer pin1 = null;
	        Integer pin2 = null;
	        Double current = null;
	        
	        Boolean notBlank = pinnummer1.getCellType() != CellType.BLANK && pinnummer2.getCellType() != CellType.BLANK 
	        		&& connector1.getCellType() != CellType.BLANK && connector2.getCellType() != CellType.BLANK
	        		&& currentValue.getCellType() != CellType.BLANK;

            if (notBlank) {
            	connectorName1 = connector1.getStringCellValue();
                pin1 = CellUtils.getIntegerValueFromCell(pinnummer1);
                if (pin1 == null) {
					pin1 = 1;
				}
                
                Contact leftContact = new Contact(connectorName1, pin1, 1);

				connectorName2 = connector2.getStringCellValue();
                pin2 = CellUtils.getIntegerValueFromCell(pinnummer2);
				if (pin2 == null) {
					pin2 = 1;
				}
                
				Contact rightContact = new Contact(connectorName2, pin2, 2);	
								
                current = CellUtils.getNumericValueFromCell(currentValue);
				if (current == null) {
					current = 0.05;
				}
				
				SimulinkSubsystem subsystem = system.findSubsystemWithContactPoints(leftContact, rightContact);
				
				if (subsystem != null) {
					ISimulinkBlock block = subsystem.getBlock("I");
					block.setParameter("i0", current);
				}
            } 

	        System.out.println();
	    }
	}
	
	private void getInformationFromExcel(Sheet sheet) {
		int rowBegin = 1; // Start from the second row
		for (int i = rowBegin; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			if (row == null) {
				continue; // Skip if row is null
			}
			Cell connector1 = row.getCell(2);
	        Cell pinnummer1 = row.getCell(3);
	        Cell connector2 = row.getCell(11);
	        Cell pinnummer2 = row.getCell(12);
	        Cell currentValue = row.getCell(7);
	        
	        String connectorName1 = null;
	        String connectorName2 = null;
	        Integer pin1 = null;
	        Integer pin2 = null;
	        Double current = null;
	        
	        Boolean notBlank = pinnummer1.getCellType() != CellType.BLANK && pinnummer2.getCellType() != CellType.BLANK 
	        		&& connector1.getCellType() != CellType.BLANK && connector2.getCellType() != CellType.BLANK
	        		&& currentValue.getCellType() != CellType.BLANK;

            if (notBlank) {
            	connectorName1 = connector1.getStringCellValue();
                pin1 = CellUtils.getIntegerValueFromCell(pinnummer1);
                if (pin1 == null) {
					pin1 = 1;
				}
                
                Contact leftContact = new Contact(connectorName1, pin1, 1);

				connectorName2 = connector2.getStringCellValue();
                pin2 = CellUtils.getIntegerValueFromCell(pinnummer2);
				if (pin2 == null) {
					pin2 = 1;
				}
                
				Contact rightContact = new Contact(connectorName2, pin2, 2);	
								
                current = CellUtils.getNumericValueFromCell(currentValue);
				if (current == null) {
					current = 0.05;
				}
				
				SimulinkSubsystem subsystem = system.findSubsystemWithContactPoints(leftContact, rightContact);
				
				if (subsystem != null) {
					ISimulinkBlock block = subsystem.getBlock("I");
					block.setParameter("i0", current);
				}
            } 

	        System.out.println();
	    }
	}

	private void init() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(KBLContainer.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			for (File kblFile : kblFiles) {
				JAXBElement<KBLContainer> rootElement = jaxbUnmarshaller.unmarshal(new StreamSource(kblFile),
						KBLContainer.class);
				kblContainer = rootElement.getValue();

				connectorHousings.addAll(kblContainer.getConnectorHousings());
				nodes.addAll(kblContainer.getNodes());
				generalWires.addAll(kblContainer.getGeneralWires());
				harnesses.add(kblContainer.getHarness());
				segments.addAll(kblContainer.getSegments());
				units.addAll(kblContainer.getUnits());
			}

			for (Harness harness : harnesses) {
				connections.addAll(harness.getConnections());
				connectorOccurrences.addAll(harness.getConnectorOccurrences());
				generalWireOccurrences.addAll(harness.getGeneralWireOccurrences());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private void generateBlocksAndConnections() {
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			ConnectorHousing connectorHousing = KBLUtils.findConnectorHousing(connectorHousings, connectorOccurrence.getPart());

			if (system.getSubsystem(connectorOccurrence.getLargeId()) == null) {
				system.addSubsystem(
						new SimulinkSubsystem(system, connectorOccurrence.getLargeId(), SubsystemType.STECKER));
				List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
				for (Cavity cavity : cavities) {
					Integer cavityNumber = KBLUtils.getCavityNumberById(connectorHousing, cavity.getPart());

					SimulinkSubsystem subsystem = system.getSubsystem(connectorOccurrence.getLargeId());
					subsystem.addInConnection(new LConnection(subsystem, cavityNumber.toString()));
					subsystem.addNumOfPins();
				}
			}
		}

		List<SimulinkSubsystem> subsystems = system.getSubsystemList(SubsystemType.STECKER);
		for (SimulinkSubsystem subsystem : subsystems) {
			subsystem.reorderConnectionsForKBL();
			subsystem.addBlock(new ElectricalReference(subsystem, subsystem.getName() + "_E"));

			List<LConnection> inPorts = subsystem.getInConnections();
			if (inPorts != null) {
				for (LConnection inPort : inPorts) {
					subsystem.addBlock(new CurrentSensor(subsystem, inPort.getName() + "_I"));
					subsystem.addBlock(new PSSimulinkConverter(subsystem, inPort.getName() + "_PS"));
					subsystem.addBlock(new Display(subsystem, inPort.getName() + "_Display"));

					subsystem.addRelation(new SimulinkRelation(inPort.getInPort(0),
							subsystem.getBlock(inPort.getName() + "_I").getInPort(0), subsystem));
					subsystem
							.addRelation(new SimulinkRelation(subsystem.getBlock(inPort.getName() + "_I").getOutPort(1),
									subsystem.getBlock(subsystem.getName() + "_E").getInPort(0), subsystem));
					subsystem
							.addRelation(new SimulinkRelation(subsystem.getBlock(inPort.getName() + "_I").getOutPort(0),
									subsystem.getBlock(inPort.getName() + "_PS").getInPort(0), subsystem));
					subsystem.addRelation(
							new SimulinkRelation(subsystem.getBlock(inPort.getName() + "_PS").getOutPort(0),
									subsystem.getBlock(inPort.getName() + "_Display").getInPort(0), subsystem));
				}
			}
		}

		for (Connection connection : connections) {
			String name;
			Double resistance = 0.0;
			Double length = null;
			Double crossSectionArea = null;
			name = connection.getSignalName();
			SimulinkSubsystem subsystem = null;

			List<Extremity> extremities = connection.getExtremities();
			Extremity startExtremity = null;
			Extremity endExtremity = null;
			ConnectorOccurrence endConnectorOccurrence = null;
			ConnectorOccurrence startConnectorOccurrence = null;
			GeneralWireOccurrence generalWireOccurrence = null;
			
			SimulinkSubsystem endStecker = null;
			SimulinkSubsystem startStecker = null;
			Integer startPin = null;
			Integer endPin = null;

			if (extremities != null) {
				for (Extremity extremity : extremities) {
					if (extremity.getPositionOnWire() == 0.0) {
						startExtremity = extremity;
					} else {
						endExtremity = extremity;
					}
				}
			}

			if (startExtremity != null && endExtremity != null) {				
				if (connection.getWire() != null) {
					generalWireOccurrence = KBLUtils.findGeneralWireOccurrence(generalWireOccurrences,
							connection.getWire());
				} 
				
				if (generalWireOccurrence != null && generalWireOccurrence.getLengthInformation() != null) {
					length = generalWireOccurrence.getLengthInformation().get(0).getLengthValue().getValueComponent();
					GeneralWire generalWire = KBLUtils.findGeneralWire(generalWires, generalWireOccurrence.getPart());
					if (generalWire != null && length != null) {
						crossSectionArea = generalWire.getCrossSectionArea().getValueComponent();
						if (crossSectionArea != null) {
							resistance = CalculatorUtils.calculateResistance(length, crossSectionArea);
							int lengthInt = (int) Math.round(length);
							name += "_" + crossSectionArea + "_" + lengthInt;
						}					
					} else {
						resistance = 0.1;
					}
				} else {
					resistance = 1.0;
				}
				
				if (system.getSubsystem(name) != null) {
					name = StringUtil.generateUniqueName(system, name);
				}

				if (system.getSubsystem(name) == null) {
					system.addSubsystem(new SimulinkSubsystem(system, name, SubsystemType.KABEL));
					subsystem = system.getSubsystem(name);
					subsystem.addInConnection(new LConnection(subsystem, "in1"));
					subsystem.addOutConnection(new RConnection(subsystem, "out1"));
					LConnection inPort = subsystem.getInConnection("in1");
					RConnection outPort = subsystem.getOutConnection("out1");

					subsystem.addBlock(new Resistor(subsystem, "R"));
					ISimulinkBlock resistor = subsystem.getBlock("R");
					resistor.setParameter("R", resistance);

					subsystem.addBlock(new DCCurrentSource(subsystem, "I"));
					ISimulinkBlock currentSource = subsystem.getBlock("I");
					if (this.currentValue != null) {
						currentSource.setParameter("i0", this.currentValue);
					} else {
						currentSource.setParameter("i0", 0.05);
					}
					
					subsystem.addBlock(new VoltageSensor(subsystem, "U"));
					ISimulinkBlock voltageSensor = subsystem.getBlock("U");

					subsystem.addBlock(new PSSimulinkConverter(subsystem, "PS"));
					ISimulinkBlock converter = subsystem.getBlock("PS");

					subsystem.addBlock(new Display(subsystem, "Display"));
					ISimulinkBlock display = subsystem.getBlock("Display");

					subsystem.addRelation(
							new SimulinkRelation(inPort.getInPort(0), currentSource.getOutPort(0), subsystem));
					subsystem.addRelation(
							new SimulinkRelation(currentSource.getInPort(0), resistor.getInPort(0), subsystem));
					subsystem
							.addRelation(new SimulinkRelation(resistor.getOutPort(0), outPort.getInPort(0), subsystem));

					subsystem.addRelation(
							new SimulinkRelation(resistor.getOutPort(0), voltageSensor.getOutPort(1), subsystem));
					subsystem.addRelation(
							new SimulinkRelation(resistor.getInPort(0), voltageSensor.getInPort(0), subsystem));
					subsystem.addRelation(
							new SimulinkRelation(voltageSensor.getOutPort(0), converter.getInPort(0), subsystem));
					subsystem.addRelation(
							new SimulinkRelation(converter.getOutPort(0), display.getInPort(0), subsystem));
				}

				if (startExtremity.getContactPoint() != null && endExtremity.getContactPoint() != null) {
					startConnectorOccurrence = KBLUtils.findConnectorOccurrenceWithContactPoint(connectorOccurrences,
							startExtremity.getContactPoint());
					if (startConnectorOccurrence != null) {
						ConnectorHousing startConnectorHousing = KBLUtils.findConnectorHousing(connectorHousings,
								startConnectorOccurrence.getPart());
						if (startConnectorHousing != null) {
							startPin = KBLUtils.findPinNumWithContactPointId(startConnectorOccurrence, startConnectorHousing,
									startExtremity.getContactPoint());
							startStecker = system.getSubsystem(startConnectorOccurrence.getLargeId());
							
							if (startPin != null) {
								String path = startConnectorOccurrence.getLargeId() + "_"
										+ startStecker.getConnectionPath(startPin.toString()) + "_" + name + "_LConn1";
								if (system.getRelation(path) == null) {
									system.addRelation(new SimulinkSubToSubRelation(startConnectorOccurrence.getLargeId(),
											startStecker.getConnectionPath(startPin.toString()), name, "LConn1", system, 0));
									Contact leftContactPoint = new Contact(startConnectorOccurrence.getLargeId(), startPin, 1);
									subsystem.addContact(leftContactPoint);
								}
							}
						}
					}
					
					
					endConnectorOccurrence = KBLUtils.findConnectorOccurrenceWithContactPoint(connectorOccurrences,
							endExtremity.getContactPoint());
					if (endConnectorOccurrence != null) {
						ConnectorHousing endConnectorHousing = KBLUtils.findConnectorHousing(connectorHousings,
								endConnectorOccurrence.getPart());
						if (endConnectorHousing != null) {

							endPin = KBLUtils.findPinNumWithContactPointId(endConnectorOccurrence, endConnectorHousing,
									endExtremity.getContactPoint());
							endStecker = system.getSubsystem(endConnectorOccurrence.getLargeId());

							if (endPin != null && startPin != null) {
								String path = endConnectorOccurrence.getLargeId() + "_"
										+ endStecker.getConnectionPath(endPin.toString()) + "_" + name + "_RConn1";
								if (system.getRelation(path) == null) {
									system.addRelation(new SimulinkSubToSubRelation(endConnectorOccurrence.getLargeId(),
											endStecker.getConnectionPath(endPin.toString()), name, "RConn1", system, 0));
									Contact rightContactPoint = new Contact(endConnectorOccurrence.getLargeId(), endPin, 2);
									subsystem.addContact(rightContactPoint);
									
									Contact startSteckerContactPoint = new Contact(endStecker.getName(), endPin, startPin);
									startStecker.addContact(startSteckerContactPoint);
									
									Contact endSteckerContactPoint = new Contact(startStecker.getName(), startPin, endPin);
									endStecker.addContact(endSteckerContactPoint);
									
									if (generalWireOccurrence != null && generalWireOccurrence.getWireNumber() != null) {
										if (generalWireOccurrence.getWireNumber() != null) {
											subsystem.setWireNumber(generalWireOccurrence.getWireNumber());	
										}								
										if (generalWireOccurrence.getId() != null) {
											subsystem.setGeneralWireOccurrenceId(generalWireOccurrence.getId());
										}
										if (generalWireOccurrence.getPart() != null) {
                                            subsystem.setGeneralWireId(name);
                                        }
									}
									
									if (length != null) {
										subsystem.setLength(length);								
									}
									if (crossSectionArea != null) {
										subsystem.setCrossSectionArea(crossSectionArea);
									}
									if (connection.getSignalName() != null) {
										subsystem.setSignalName(connection.getSignalName());
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
