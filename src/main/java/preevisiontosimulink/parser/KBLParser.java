package preevisiontosimulink.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import preevisiontosimulink.library.InPort;
import preevisiontosimulink.library.LConnection;
import preevisiontosimulink.parser.kblelements.Cavity;
import preevisiontosimulink.parser.kblelements.Connection;
import preevisiontosimulink.parser.kblelements.ConnectorHousing;
import preevisiontosimulink.parser.kblelements.ConnectorOccurrence;
import preevisiontosimulink.parser.kblelements.Extremity;
import preevisiontosimulink.parser.kblelements.GeneralWire;
import preevisiontosimulink.parser.kblelements.GeneralWireOccurrence;
import preevisiontosimulink.parser.kblelements.Harness;
import preevisiontosimulink.parser.kblelements.KBLContainer;
import preevisiontosimulink.parser.kblelements.Node;
import preevisiontosimulink.parser.kblelements.Segment;
import preevisiontosimulink.parser.kblelements.Unit;
import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.relation.SimulinkSubToSubRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSubsystemType;
import preevisiontosimulink.proxy.system.SimulinkSystem;
import preevisiontosimulink.proxy.system.SimulinkSystemType;
import preevisiontosimulink.util.CalculatorUtils;
import preevisiontosimulink.util.ExcelGenerator;
import preevisiontosimulink.util.KBLModifier;
import preevisiontosimulink.util.KBLUtils;
import preevisiontosimulink.util.SimulinkSubsystemInitHelper;
import preevisiontosimulink.util.StringUtils;

public class KBLParser {
	private SimulinkSystem system;
	private String modelName;
	private List<File> kblFiles = new ArrayList<>();
	private List<File> xlsxFiles = new ArrayList<>();

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

	public KBLParser(String modelName, List<String> paths) {
		this.modelName = modelName;
		for (String path : paths) {
			File file = new File(path);
			if (path.endsWith(".kbl")) {
				kblFiles.add(file);
			} else if (path.endsWith(".xlsx")) {
				xlsxFiles.add(file);
			}
		}
		init();
	}

	public void generateModel() {
		system = new SimulinkSystem(modelName, SimulinkSystemType.WIRING_HARNESS, null);

		getWiringHarness();

		system.generateModel();
	}
	
	public void getGeneralWireInformation() {
		system = new SimulinkSystem(modelName, null, null);

		getWiringHarness();
	}

	public void generateExcel() {
		system = new SimulinkSystem(modelName, SimulinkSystemType.WIRING_HARNESS, null);

		getWiringHarness();

		ExcelGenerator.generateExcel(modelName, system);
	}

	public void generateUpdatedExcel() {
		ExcelGenerator.generateUpdatedExcel(modelName, system);
	}

	public void generateModifiedKBL() {
		system = new SimulinkSystem(modelName, SimulinkSystemType.WIRING_HARNESS, null);

		getWiringHarness();

		KBLModifier.generateModifiedKBL(system, xlsxFiles, kblFiles);
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

	private void getWiringHarness() {
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			parsingConnectorOccurrences(connectorOccurrence);
			generateConnectorOccurrences(connectorOccurrence, SimulinkSubsystemType.STECKER);
		}
		
		for (Connection connection : connections) {
			parsingConnection(connection);
		}
		
		for (Connection connection : connections) {
			if (connection.getStartConnector() != null && connection.getEndConnector() != null
					&& connection.getStartPin() != null && connection.getEndPin() != null) {
				generateConnection(connection, SimulinkSubsystemType.KABEL);
			}
		}
	}
	
	private void parsingConnectorOccurrences(ConnectorOccurrence connectorOccurrence) {
        ConnectorHousing connectorHousing = null;
        if (connectorOccurrence.getPart() != null) {
            connectorHousing = KBLUtils.findConnectorHousing(connectorHousings, connectorOccurrence.getPart());
        }
        
        if (connectorHousing != null) {
        	connectorOccurrence.setConnectorHousing(connectorHousing);
        }
	}
	
	private void generateConnectorOccurrences(ConnectorOccurrence connectorOccurrence, SimulinkSubsystemType type) {
		ConnectorHousing connectorHousing = connectorOccurrence.getConnectorHousing();
        if (system.getSubsystem(connectorOccurrence.getLargeId()) == null && connectorHousing != null) {
        	SimulinkSubsystem subsystem = system.addSubsystem(new SimulinkSubsystem(system, connectorOccurrence.getLargeId(), type));
            List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
            for (Cavity cavity : cavities) {
                Integer cavityNumber = KBLUtils.getCavityNumberById(connectorHousing, cavity.getPart());
                
                if (type == SimulinkSubsystemType.STECKER) {
                	subsystem.addInConnection(new LConnection(subsystem, cavityNumber.toString()));
                } else if (type == SimulinkSubsystemType.THERMAL_STECKER) {
                	subsystem.addInPort(new InPort(subsystem, cavityNumber.toString()));
                }                
                subsystem.addNumOfPins();
            }
    		SimulinkSubsystemInitHelper.initStecker(subsystem);
        }
	}
	
	private void parsingConnection(Connection connection) {
        String name = connection.getSignalName();
        Double resistance = 0.1;
        Double length = null;
        Double crossSectionArea = null;

        Extremity startExtremity = null;
        Extremity endExtremity = null;
        GeneralWireOccurrence generalWireOccurrence = null;
        GeneralWire generalWire = null;

        Integer startPin = null;
        Integer endPin = null;

        if (connection.getExtremities() != null) {
            for (Extremity extremity : connection.getExtremities()) {
                if (extremity.getPositionOnWire() == 0.0) {
                    startExtremity = extremity;
                } else {
                    endExtremity = extremity;
                }
            }
        }

        if (startExtremity != null && endExtremity != null) {
            if (connection.getWire() != null) {
                generalWireOccurrence = KBLUtils.findGeneralWireOccurrence(generalWireOccurrences, connection.getWire());
            }
            
            if (generalWireOccurrence != null) {
            	connection.setGeneralWireOccurrence(generalWireOccurrence);
                generalWire = KBLUtils.findGeneralWire(generalWires, generalWireOccurrence.getPart());
            }
            
            if (generalWire != null) {
            	generalWire.addConnection(connection);
            }

            if (generalWireOccurrence != null && generalWireOccurrence.getLengthInformation() != null) {
                length = generalWireOccurrence.getLengthInformation().get(0).getLengthValue().getValueComponent();
                if (generalWire != null && length != null) {
                    crossSectionArea = generalWire.getCrossSectionArea().getValueComponent();
                    if (crossSectionArea != null) {
                        resistance = CalculatorUtils.calculateResistance(length, crossSectionArea);                     
                    }
                }
            }
            
            if (crossSectionArea != null) {
            	name += "_" + crossSectionArea;
            	connection.setCrossSectionArea(crossSectionArea);
            }
            if (length != null) {
                int lengthInt = (int) Math.round(length);  
                name += "_" + lengthInt;
                connection.setLength(length);
            }
            
            connection.setName(name);
            connection.setResistance(resistance);         

            if (startExtremity.getContactPoint() != null && endExtremity.getContactPoint() != null) {
            	ConnectorOccurrence startConnectorOccurrence = KBLUtils.findConnectorOccurrenceWithContactPoint(connectorOccurrences, startExtremity.getContactPoint());
                if (startConnectorOccurrence != null) {
                	connection.setStartConnector(startConnectorOccurrence);
                    ConnectorHousing startConnectorHousing = KBLUtils.findConnectorHousing(connectorHousings, startConnectorOccurrence.getPart());
                    
                    if (startConnectorHousing != null) {
                    	startConnectorOccurrence.setConnectorHousing(startConnectorHousing);
                        startPin = KBLUtils.findPinNumWithContactPointId(startConnectorOccurrence, startConnectorHousing, startExtremity.getContactPoint());

                        if (startPin != null) {
                        	connection.setStartPin(startPin);     
                        }
                    }
                }

                ConnectorOccurrence endConnectorOccurrence = KBLUtils.findConnectorOccurrenceWithContactPoint(connectorOccurrences, endExtremity.getContactPoint());
                if (endConnectorOccurrence != null) {
                	connection.setEndConnector(endConnectorOccurrence);
                    ConnectorHousing endConnectorHousing = KBLUtils.findConnectorHousing(connectorHousings, endConnectorOccurrence.getPart());
                    if (endConnectorHousing != null) {
                    	endConnectorOccurrence.setConnectorHousing(endConnectorHousing);
                        endPin = KBLUtils.findPinNumWithContactPointId(endConnectorOccurrence, endConnectorHousing, endExtremity.getContactPoint());

                        if (endPin != null) {
                        	connection.setEndPin(endPin);                               
                        }
                    }
                }
                if (length != null) {
                	connection.setLength(length);
                }
                if (crossSectionArea != null) {
                	connection.setCrossSectionArea(crossSectionArea);
                }
            }
        }
	}
	
	private void generateConnection(Connection connection, SimulinkSubsystemType type) {
        	String name = connection.getName();
            if (system.getSubsystem(name) != null) {
                name = StringUtils.generateUniqueName(system, name);
            }
            system.addSubsystem(new SimulinkSubsystem(system, name, type));
            SimulinkSubsystem subsystem = system.getSubsystem(name);
            
            switch (type) {
            case KABEL:
            	SimulinkSubsystemInitHelper.initKabel(subsystem, connection.getResistance());
                break;
            case THERMAL_KABEL:
                
                break;
            // Add other cases if there are more types in SimulinkSubsystemType
            default:
                // Handle unexpected types if necessary
                break;
            }
            
            Integer startPin = connection.getStartPin();
            String startConnectorName = connection.getStartConnector().getLargeId();
            SimulinkSubsystem startStecker = system.getSubsystem(startConnectorName);     
            Integer endPin = connection.getEndPin();
            String endConnectorName = connection.getEndConnector().getLargeId();
            SimulinkSubsystem endStecker = system.getSubsystem(endConnectorName);
            
            String pathStart = startConnectorName + "_" + startStecker.getConnectionPath(startPin.toString()) + "_" + name + "_LConn1";
            if (system.getRelation(pathStart) == null) {
                system.addRelation(new SimulinkSubToSubRelation(startConnectorName, startStecker.getConnectionPath(startPin.toString()), name, "LConn1", system, 0));
                Contact leftContactPoint = new Contact(startConnectorName, startPin, 1);
                subsystem.addContact(leftContactPoint);
				Contact startSteckerContactPoint = new Contact(endStecker.getName(), endPin, startPin);
				startStecker.addContact(startSteckerContactPoint);
            }
            
            String pathEnd = endConnectorName + "_" + endStecker.getConnectionPath(endPin.toString()) + "_" + name + "_RConn1";
            if (system.getRelation(pathEnd) == null) {
                system.addRelation(new SimulinkSubToSubRelation(endConnectorName, endStecker.getConnectionPath(endPin.toString()), name, "RConn1", system, 0));
                Contact rightContactPoint = new Contact(endConnectorName, endPin, 2);
                subsystem.addContact(rightContactPoint);
				Contact endSteckerContactPoint = new Contact(startStecker.getName(), startPin, endPin);
				endStecker.addContact(endSteckerContactPoint);
            }
            
            GeneralWireOccurrence generalWireOccurrence = connection.getGeneralWireOccurrence();
            if (generalWireOccurrence != null && generalWireOccurrence.getWireNumber() != null) {
                if (generalWireOccurrence.getWireNumber() != null) {
                    subsystem.getKabelInformation().setWireNumber(generalWireOccurrence.getWireNumber());
                }
                if (generalWireOccurrence.getId() != null) {
                    subsystem.getKabelInformation().setGeneralWireOccurrenceId(generalWireOccurrence.getId());
                }
                if (generalWireOccurrence.getPart() != null) {
                    subsystem.getKabelInformation().setGeneralWireId(generalWireOccurrence.getPart());
                }
            }
            if (connection.getLength() != null) {
                subsystem.getKabelInformation().setLength(connection.getLength());
            }
            if (connection.getCrossSectionArea() != null) {
                subsystem.getKabelInformation().setCrossSectionArea(connection.getCrossSectionArea());
            }
            if (connection.getSignalName() != null) {
                subsystem.getKabelInformation().setSignalName(connection.getSignalName());
            }
	}

	public List<GeneralWire> getGeneralWires() {
		return generalWires;
	}
	
	public List<GeneralWire> getValidGeneralWires() {
        return generalWires.stream()
                .filter(GeneralWire::isValid)
                .collect(Collectors.toList());                                                 
	}
}
