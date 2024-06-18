package preevisiontosimulink.parser;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import preevisiontosimulink.util.CellUtils;
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
	    
        // Sortiere die Subsysteme nach dem Namen des ersten Kontaktpunkts, wenn dieser nicht null ist
        Collections.sort(filteredSubsystems, new Comparator<SimulinkSubsystem>() {
            @Override
            public int compare(SimulinkSubsystem subsystem1, SimulinkSubsystem subsystem2) {
                String name1 = subsystem1.getContactPoints().get(0).getName();
                String name2 = subsystem2.getContactPoints().get(0).getName();
                return name1.compareTo(name2);
            }
        });

	    // Create data rows
	    for (int i = 1; i <= filteredSubsystems.size(); i++) {
	        Row row = sheet.createRow(i);

	        SimulinkSubsystem subsystem = filteredSubsystems.get(i - 1);
	        row.createCell(2).setCellValue(subsystem.getContactPoints().get(0).getName());
            row.createCell(11).setCellValue(subsystem.getContactPoints().get(1).getName());
            if (subsystem.getCrossSectionArea() != null) {
            	row.createCell(20).setCellValue(subsystem.getCrossSectionArea());
            }
            if (subsystem.getLength() != null) {
            	row.createCell(21).setCellValue(subsystem.getLength());
            }
	    }

	    // Resize columns to fit the content
	    for (int i = 0; i < 24; i++) {
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
			ConnectorHousing connectorHousing = findConnectorHousing(connectorHousings, connectorOccurrence.getPart());

			if (system.getSubsystem(connectorOccurrence.getLargeId()) == null) {
				system.addSubsystem(
						new SimulinkSubsystem(system, connectorOccurrence.getLargeId(), SubsystemType.STECKER));
				List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
				for (Cavity cavity : cavities) {
					Integer cavityNumber = getCavityNumberById(connectorHousing, cavity.getPart());

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
				GeneralWireOccurrence generalWireOccurrence = null;
				if (connection.getWire() != null) {
					generalWireOccurrence = findGeneralWireOccurrence(generalWireOccurrences,
							connection.getWire());
				} 
				
				if (generalWireOccurrence != null && generalWireOccurrence.getLengthInformation() != null) {
					length = generalWireOccurrence.getLengthInformation().get(0).getLengthValue().getValueComponent();
					GeneralWire generalWire = findGeneralWire(generalWires, generalWireOccurrence.getPart());
					if (generalWire != null && length != null) {
						crossSectionArea = generalWire.getCrossSectionArea().getValueComponent();
						if (crossSectionArea != null) {
							resistance = calculateResistance(length, crossSectionArea);
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
					name = generateUniqueName(system, name);
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

				if (startExtremity != null && startExtremity.getContactPoint() != null) {
					startConnectorOccurrence = findConnectorOccurrenceWithContactPoint(connectorOccurrences,
							startExtremity.getContactPoint());
					if (startConnectorOccurrence != null) {
						ConnectorHousing startConnectorHousing = findConnectorHousing(connectorHousings,
								startConnectorOccurrence.getPart());
						if (startConnectorHousing != null) {
							startPin = findPinNumWithContactPointId(startConnectorOccurrence, startConnectorHousing,
									startExtremity.getContactPoint());
							startStecker = system.getSubsystem(startConnectorOccurrence.getLargeId());

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

				if (endExtremity != null && endExtremity.getContactPoint() != null) {
					endConnectorOccurrence = findConnectorOccurrenceWithContactPoint(connectorOccurrences,
							endExtremity.getContactPoint());
					if (endConnectorOccurrence != null) {
						ConnectorHousing endConnectorHousing = findConnectorHousing(connectorHousings,
								endConnectorOccurrence.getPart());
						if (endConnectorHousing != null) {

							endPin = findPinNumWithContactPointId(endConnectorOccurrence, endConnectorHousing,
									endExtremity.getContactPoint());
							endStecker = system.getSubsystem(endConnectorOccurrence.getLargeId());

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
								
								if (findGeneralWireOccurrence(generalWireOccurrences, connection.getId()) != null 
										&& findGeneralWireOccurrence(generalWireOccurrences, connection.getId()).getWireNumber() != null) {
									subsystem.setWireNumber(findGeneralWireOccurrence(generalWireOccurrences, connection.getId()).getWireNumber());										
								}
								
								if (length != null) {
									subsystem.setLength(length);								
								}
								if (crossSectionArea != null) {
									subsystem.setCrossSectionArea(crossSectionArea);
								}
							}
						}
					}
				}
			}
		}
	}

	private GeneralWire findGeneralWire(List<GeneralWire> generalWires, String id) {
		if (generalWires != null) {
			for (GeneralWire generalWire : generalWires) {
				if (generalWire.getId().equals(id)) {
					return generalWire;
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	private ConnectorHousing findConnectorHousing(List<ConnectorHousing> connectorHousings, String id) {
		if (connectorHousings != null) {
			for (ConnectorHousing connectorHousing : connectorHousings) {
				if (connectorHousing.getId().equals(id)) {
					return connectorHousing;
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	private ConnectorOccurrence findConnectorOccurrenceWithContactPoint(List<ConnectorOccurrence> connectorOccurrences,
			String id) {
		if (connectorOccurrences != null) {
			for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
				List<ContactPoint> contactPoints = connectorOccurrence.getContactPoints();
				if (contactPoints != null) {
					for (ContactPoint contactPoint : contactPoints) {
						if (contactPoint.getId().equals(id)) {
							return connectorOccurrence;
						}
					}
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	private Integer findPinNumWithContactPointId(ConnectorOccurrence connectorOccurrence,
			ConnectorHousing connectorHousing, String id) {
		if (connectorOccurrence != null && connectorHousing != null) {
			List<ContactPoint> contactPoints = connectorOccurrence.getContactPoints();
			ContactPoint contactPoint = null;
			Cavity cavityInConnectorOccurrence = null;
			Cavity cavityInConnectorHousing = null;
			for (ContactPoint contact : contactPoints) {
				if (contact.getId().equals(id)) {
					contactPoint = contact;
				}
			}
			if (contactPoint == null) {
				return null;
			}
			cavityInConnectorOccurrence = findCavityInConnectorOccurrence(connectorOccurrence,
					contactPoint.getContactedCavity());
			cavityInConnectorHousing = findCavityInConnectorHousing(connectorHousing,
					cavityInConnectorOccurrence.getPart());
			if (cavityInConnectorHousing != null) {
				return cavityInConnectorHousing.getCavityNumber();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private Cavity findCavityInConnectorOccurrence(ConnectorOccurrence connectorOccurrence, String id) {
		if (connectorOccurrence != null) {
			List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
			for (Cavity cavity : cavities) {
				if (cavity.getId().equals(id)) {
					return cavity;
				}
			}
		}

		return null; // If no corresponding CartesianPoint is found
	}

	private Cavity findCavityInConnectorHousing(ConnectorHousing connectorHousing, String id) {
		if (connectorHousing == null || connectorHousing.getSlots() == null) {
			return null;
		}
		
		List<Cavity> cavities = connectorHousing.getSlots().getCavities();
		for (Cavity cavity : cavities) {
			if (cavity.getId().equals(id)) {
				return cavity;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	private ConnectorOccurrence findConnectorOccurrence(List<ConnectorOccurrence> connectorOccurrences,
			String largeId) {
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			if (connectorOccurrence.getLargeId().equals(largeId)) {
				return connectorOccurrence;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	private Node findNode(List<Node> nodes, String id) {
		for (Node node : nodes) {
			if (node.getId().equals(id)) {
				return node;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}
	
	private Connection findConnection(List<Connection> connections, String id) {
		for (Connection connection : connections) {
			if (connection.getId().equals(id)) {
				return connection;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	private Integer getCavityNumberById(ConnectorHousing connectorHousing, String cavityId) {
		if (connectorHousing == null || connectorHousing.getSlots() == null) {
			return null;
		}

		Slots slots = connectorHousing.getSlots();
		List<Cavity> cavities = slots.getCavities();

		if (cavities != null) {
			for (Cavity cavity : cavities) {
				if (cavityId.equals(cavity.getId())) {
					return cavity.getCavityNumber();
				}
			}
		}

		return null; // Return null if the cavity with the given ID is not found
	}
	
	

	private GeneralWireOccurrence findGeneralWireOccurrence(List<GeneralWireOccurrence> generalWireOccurrences,
			String id) {
		if (generalWireOccurrences == null) {
			return null;
		}
		for (GeneralWireOccurrence generalWireOccurrence : generalWireOccurrences) {
			if (generalWireOccurrence.getId().equals(id)) {
				return generalWireOccurrence;
			}
		}
		return null; // If no corresponding CartesianPoint is found
	}

	private double calculateResistance(double length, double crossSectionalArea) {
		// Convert cross-sectional area from mm² to m² (1 mm² = 1e-6 m²)
		double crossSectionalAreaM2 = crossSectionalArea * 1e-6;

		// Convert length from mm to meters (1 mm = 1e-3 m)
		double lengthM = length * 1e-3;

		// Calculate and return resistance using the formula R = ρ * (L / A)
		return 1.77e-8 * (lengthM / crossSectionalAreaM2);
	}
	
    // Recursive method to generate a unique name
    private String generateUniqueName(SimulinkSystem system, String name) {
        if (system.getSubsystem(name) != null) {
            // If the name exists, append or increment the suffix and try again
            int suffix = 1;
            String newName;
            do {
                newName = name + "_" + suffix;
                suffix++;
            } while (system.getSubsystem(newName) != null);
            return generateUniqueName(system, newName);
        } else {
            // If the name is unique, return it
            return name;
        }
    }
}
