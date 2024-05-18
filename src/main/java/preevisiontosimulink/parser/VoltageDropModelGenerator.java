package preevisiontosimulink.parser;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import preevisiontosimulink.parser.kblelements.*;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkExternRelation;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;

import java.io.File;
import java.util.List;

public class VoltageDropModelGenerator {
	private SimulinkSystem system;
	private String modelName;
	private File kblFile;
	
	private KBLContainer kblContainer;
	private List<ConnectorHousing> connectorHousings;
	private List<Node> nodes;
	private List<GeneralWire> generalWires;
	private Harness harness;
	private List<Segment> segments;
	private List<Unit> units;
	private List<Connection> connections;
	private List<ConnectorOccurrence> connectorOccurrences;
	private List<GeneralWireOccurrence> generalWireOccurrences;
	
	public VoltageDropModelGenerator(String modelName, String kblFilePath) {
		this.modelName = modelName;
		init(kblFilePath);
	}
	
    public void generateModel() {
        system = new SimulinkSystem(modelName);
        SimulinkSubsystem lastSubsystem;
        
        generateBlocks();	
        generateConnections();
        
        system.generateModel();
    }
    
    private void init(String kblFilePath) {
    	try {
    		kblFile = new File(kblFilePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(KBLContainer.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            JAXBElement<KBLContainer> rootElement = jaxbUnmarshaller.unmarshal(new StreamSource(kblFile), KBLContainer.class);
            kblContainer = rootElement.getValue();
            
            connectorHousings = kblContainer.getConnectorHousings();
            nodes = kblContainer.getNodes();
			generalWires = kblContainer.getGeneralWires();
			harness = kblContainer.getHarness();
			segments = kblContainer.getSegments();
			units = kblContainer.getUnits();		
			nodes = kblContainer.getNodes();
			segments = kblContainer.getSegments();
			
			connections = harness.getConnections();
			connectorOccurrences = harness.getConnectorOccurrences();
			generalWireOccurrences = harness.getGeneralWireOccurrences();
    	} catch (JAXBException e) {
            e.printStackTrace();
        }
	}
    
    private void generateBlocks() {
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			ConnectorHousing connectorHousing = findConnectorHousing(connectorHousings, connectorOccurrence.getPart());
			if (system.getSubsystem(connectorHousing.getPartNumber()) == null) {
				system.addSubsystem(new SimulinkSubsystem(system, connectorHousing.getDescription()));
			}
		}
		
		for (Connection connection : connections) {
			String name;
			double resistance = 0;
			double length = 0;
			double crossSectionArea = 0;
			name = connection.getId() + "_" + connection.getSignalName();
			system.addBlock(new Resistor(system, name));
			GeneralWireOccurrence generalWireOccurrence = findGeneralWireOccurrence(generalWireOccurrences, connection.getWire());
			if (generalWireOccurrence != null) {
				length = generalWireOccurrence.getLengthInformation().getLengthValue().getValueComponent();
				GeneralWire generalWire = findGeneralWire(generalWires, generalWireOccurrence.getPart());
				if (generalWire != null) {
					crossSectionArea = generalWire.getCrossSectionArea().getValueComponent();
					resistance = calculateResistance(length, crossSectionArea);
				}
			} else {
				resistance = 0.1;
			}
			system.getBlock(name).setParameter("R", resistance);
		}
		
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			ConnectorHousing connectorHousing = findConnectorHousing(connectorHousings, connectorOccurrence.getPart());
			SimulinkSubsystem subsystem = system.getSubsystem(connectorHousing.getDescription());
			subsystem.addInPort(new LConnection(subsystem, connectorOccurrence.getLargeId()));
		}
		
		for (Segment segment : segments) {
			String name;
			double resistance = 0;
			double length = 0;
			double crossSectionArea = 0;
			name = segment.getLargeId();
			system.addBlock(new Resistor(system, name));
			length = segment.getPhysicalLength().getValueComponent();
			if (segment.getCrossSectionAreaInformation() != null) {
				crossSectionArea = segment.getCrossSectionAreaInformation().getArea().getValueComponent();
			} else {
				crossSectionArea = 50;
			}			
			resistance = calculateResistance(length, crossSectionArea);
			system.getBlock(name).setParameter("R", resistance);
		}		
	}
    
    private void generateConnections() {
    	for (Connection connection : connections) {
    		String name = connection.getId() + "_" + connection.getSignalName();
	    	List <Extremity> extremities = connection.getExtremities();
	    	Extremity firstExtremity = extremities.get(0);
	    	Extremity secondExtremity = extremities.get(1);
	    	
	    	ConnectorOccurrence firstConnectorOccurrence = findConnectorOccurrence(connectorOccurrences, firstExtremity.getContactPoint());
	    	ConnectorOccurrence secondConnectorOccurrence = findConnectorOccurrence(connectorOccurrences, secondExtremity.getContactPoint());
	    	
	    	ConnectorHousing firstConnectorHousing = findConnectorHousing(connectorHousings, firstConnectorOccurrence.getPart());
	    	ConnectorHousing secondConnectorHousing = findConnectorHousing(connectorHousings, secondConnectorOccurrence.getPart());
	    	
	    	SimulinkSubsystem firstSubsystem = system.getSubsystem(firstConnectorHousing.getDescription());
	    	SimulinkSubsystem secondSubsystem = system.getSubsystem(secondConnectorHousing.getDescription());	    	
	    	
			system.addRelation(new SimulinkExternRelation(system.getBlock(name).getInPort(0), firstConnectorHousing.getDescription(), firstSubsystem.getPortPath(firstConnectorOccurrence.getLargeId()), system));
			system.addRelation(new SimulinkExternRelation(system.getBlock(name).getOutPort(0), secondConnectorHousing.getDescription(), secondSubsystem.getPortPath(secondConnectorOccurrence.getLargeId()), system));
    	}
	}
    
    private GeneralWire findGeneralWire(List<GeneralWire> generalWires, String id) {
        for (GeneralWire generalWire : generalWires) {
            if (generalWire.getId().equals(id)) {
                return generalWire;
            }
        }
        return null; // If no corresponding CartesianPoint is found
    }
    
    private ConnectorHousing findConnectorHousing(List<ConnectorHousing> connectorHousings, String id) {
        for (ConnectorHousing connectorHousing : connectorHousings) {
            if (connectorHousing.getId().equals(id)) {
                return connectorHousing;
            }
        }
        return null; // If no corresponding CartesianPoint is found
    }
    
    private ConnectorOccurrence findConnectorOccurrence(List<ConnectorOccurrence> connectorOccurrences, String id) {
        for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
            List <ContactPoint> contactPoints = connectorOccurrence.getContactPoints();
            for (ContactPoint contactPoint : contactPoints) {
				if (contactPoint.getId().equals(id)) {
					return connectorOccurrence;
				}
			}
        }
        return null; // If no corresponding CartesianPoint is found
    }
    
    private GeneralWireOccurrence findGeneralWireOccurrence(List<GeneralWireOccurrence> generalWireOccurrences, String id) {
        for (GeneralWireOccurrence generalWireOccurrence : generalWireOccurrences) {
            if (generalWireOccurrence.getId().equals(id)) {
                return generalWireOccurrence;
            }
        }
        return null; // If no corresponding CartesianPoint is found
    }
    
    public static double calculateResistance(double length, double crossSectionalArea) {
        // Convert cross-sectional area from mm² to m² (1 mm² = 1e-6 m²)
        double crossSectionalAreaM2 = crossSectionalArea * 1e-6;

        // Convert length from mm to meters (1 mm = 1e-3 m)
        double lengthM = length * 1e-3;

        // Calculate and return resistance using the formula R = ρ * (L / A)
        return 1.77e-8 * (lengthM / crossSectionalAreaM2);
    }
}
