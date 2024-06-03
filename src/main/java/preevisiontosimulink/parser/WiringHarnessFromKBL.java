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
import java.util.ArrayList;
import java.util.List;

public class WiringHarnessFromKBL {
	private SimulinkSystem system;
	private String modelName;
	private List<File> kblFiles = new ArrayList<>();
	
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
		
    public WiringHarnessFromKBL(String modelName, List<String> kblFilePaths) {
        this.modelName = modelName;
        for (String path : kblFilePaths) {
            kblFiles.add(new File(path));
        }
        init();
    }
	
    public void generateModel() {
        system = new SimulinkSystem(modelName);
        
        generateBlocksAndConnections();	
        //generateConnections();
        
        system.generateModel();
    }
    
    private void init() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(KBLContainer.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            
            for (File kblFile : kblFiles) {
                JAXBElement<KBLContainer> rootElement = jaxbUnmarshaller.unmarshal(new StreamSource(kblFile), KBLContainer.class);
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
				system.addSubsystem(new SimulinkSubsystem(system, connectorOccurrence.getLargeId()));
				List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
				for (Cavity cavity : cavities) {
					Integer cavityNumber = getCavityNumberById(connectorHousing, cavity.getPart());
					
					SimulinkSubsystem subsystem = system.getSubsystem(connectorOccurrence.getLargeId());
					subsystem.addInConnection(new LConnection(subsystem, cavityNumber.toString()));
					subsystem.addBlock(new Resistor(subsystem, cavityNumber.toString() + "_R"));
					subsystem.getBlock(cavityNumber.toString() + "_R").setParameter("R", 5);
				}
			}
		}
		
		List <SimulinkSubsystem> subsystems = system.getSubsystemList();
		for (SimulinkSubsystem subsystem : subsystems) {
			subsystem.reorderConnectionsForKBL();
			
			List <LConnection> inPorts = subsystem.getInConnections();
			if (inPorts != null) {
				for (LConnection inPort : inPorts) {
					subsystem.addRelation(new SimulinkRelation(inPort.getInPort(0), subsystem.getBlock(inPort.getName() + "_R").getInPort(0), subsystem));
				}
				if (inPorts.size() > 1) {
					LConnection endPort = inPorts.get(inPorts.size() - 1);
					for (int i = 0; i < inPorts.size() - 1; i++) {						
						LConnection startPort = inPorts.get(i);
						ISimulinkBlock endResistor = subsystem.getBlock(endPort.getName() + "_R");
						ISimulinkBlock startResistor = subsystem.getBlock(startPort.getName() + "_R");
						subsystem.addRelation(new SimulinkRelation(endResistor.getOutPort(0), startResistor.getOutPort(0), subsystem));
					}
				} else {
					LConnection startPort = inPorts.get(0);
					ISimulinkBlock startResistor = subsystem.getBlock(startPort.getName() + "_R");
					subsystem.addBlock(new ElectricalReference(subsystem, startPort.getName() + "_E"));
					subsystem.addRelation(new SimulinkRelation(startResistor.getOutPort(0), subsystem.getBlock(startPort.getName() + "_E").getInPort(0), subsystem));
				}
			}
		}
				
		for (Connection connection : connections) {
			String name;
			Double resistance;
			Double length;
			Double crossSectionArea;
			name = connection.getId() + "_" + connection.getSignalName();
						
	    	List <Extremity> extremities = connection.getExtremities();
	    	Extremity startExtremity = null;
	    	Extremity endExtremity = null;
	    	ConnectorOccurrence startConnectorOccurrence = null;
	    	ConnectorOccurrence endConnectorOccurrence = null;
	    	
	    	if (extremities != null) {
		    	for (Extremity extremity : extremities) {
			    	if (extremity.getPositionOnWire() == 0.0) {
			    		startExtremity = extremity;	
			    	} else {
				    	endExtremity = extremity;
			    	}
		    	}
	    	}
	    	
	    	if (startExtremity != null || endExtremity != null) {		
	    		if (system.getBlock(name) == null) {	
	    			system.addBlock(new Resistor(system, name));					
	    		}
	    		
				GeneralWireOccurrence generalWireOccurrence = findGeneralWireOccurrence(generalWireOccurrences, connection.getWire());
				if (generalWireOccurrence != null) {
					length = generalWireOccurrence.getLengthInformation().getLengthValue().getValueComponent();
					GeneralWire generalWire = findGeneralWire(generalWires, generalWireOccurrence.getPart());
					if (generalWire != null && length != null) {
						crossSectionArea = generalWire.getCrossSectionArea().getValueComponent();
						resistance = calculateResistance(length, crossSectionArea);							
						system.getBlock(name).setParameter("R", resistance);
					} else {
						resistance = 0.1;
						system.getBlock(name).setParameter("R", resistance);
					}					
				}
			
				if (startExtremity != null) {
		    		startConnectorOccurrence = findConnectorOccurrenceWithContactPoint(connectorOccurrences, startExtremity.getContactPoint());
		    		ConnectorHousing startConnectorHousing = findConnectorHousing(connectorHousings, startConnectorOccurrence.getPart());
		    		Integer startPin = findPinNumWithContactPointId(startConnectorOccurrence, startConnectorHousing, startExtremity.getContactPoint());		    				    		
		    		SimulinkSubsystem startSubsystem = system.getSubsystem(startConnectorOccurrence.getLargeId());
		    		String nameStartConnection = name + "_" + startConnectorOccurrence.getLargeId() + "_" + startSubsystem.getConnectionPath(startPin.toString());
		    		if (system.getRelation(nameStartConnection) == null) {
		    			system.addRelation(new SimulinkExternRelation(system.getBlock(name).getInPort(0), startConnectorOccurrence.getLargeId(), startSubsystem.getConnectionPath(startPin.toString()), system, 0));
		    		}		    		
				}
				if (endExtremity != null) {
			    	endConnectorOccurrence = findConnectorOccurrenceWithContactPoint(connectorOccurrences, endExtremity.getContactPoint());		    			    	
			    	ConnectorHousing endConnectorHousing = findConnectorHousing(connectorHousings, endConnectorOccurrence.getPart());		    			    	
			    	Integer endPin = findPinNumWithContactPointId(endConnectorOccurrence, endConnectorHousing, endExtremity.getContactPoint());
			    	SimulinkSubsystem endSubsystem = system.getSubsystem(endConnectorOccurrence.getLargeId());
			    	String nameEndConnection = name + "_" + endConnectorOccurrence.getLargeId() + "_" + endSubsystem.getConnectionPath(endPin.toString());
			    	if (system.getRelation(nameEndConnection) == null) {
			    	system.addRelation(new SimulinkExternRelation(system.getBlock(name).getOutPort(0), endConnectorOccurrence.getLargeId(), endSubsystem.getConnectionPath(endPin.toString()), system, 0));
			    	}
				}
	    	}
		}
		
		/*
		for (Segment segment : segments) {
			String name;
			double resistance = 0;
			double length = 0;
			double crossSectionArea = 0;
			name = segment.getLargeId();
			
			Node firstNode = findNode(nodes, segment.getStartNode());
			Node secondNode = findNode(nodes, segment.getEndNode());
			
			if (firstNode != null && secondNode != null && firstNode.getReferencedComponents() != null && secondNode.getReferencedComponents() != null) {
				system.addBlock(new Resistor(system, name));
				if (segment.getPhysicalLength() != null) {
					length = segment.getPhysicalLength().getValueComponent();
					if (segment.getCrossSectionAreaInformation() != null) {
						crossSectionArea = segment.getCrossSectionAreaInformation().getArea().getValueComponent();
						resistance = calculateResistance(length, crossSectionArea);
					} else {
						resistance = 0.1E-4;
					}
				} 
				
				system.getBlock(name).setParameter("R", resistance);
				
				ConnectorOccurrence firstConnectorOccurrence = findConnectorOccurrence(connectorOccurrences, firstNode.getReferencedComponents());
				ConnectorHousing firstConnectorHousing = findConnectorHousing(connectorHousings, firstConnectorOccurrence.getPart());
				SimulinkSubsystem firstSubsystem = system.getSubsystem(firstConnectorHousing.getDescription());
				system.addRelation(new SimulinkExternRelation(system.getBlock(name).getInPort(0), firstConnectorHousing.getDescription(), firstSubsystem.getConnectionPath(firstConnectorOccurrence.getLargeId()), system, 0));
				
				ConnectorOccurrence secondConnectorOccurrence = findConnectorOccurrence(connectorOccurrences, secondNode.getReferencedComponents());
				ConnectorHousing secondConnectorHousing = findConnectorHousing(connectorHousings, secondConnectorOccurrence.getPart());
				SimulinkSubsystem secondSubsystem = system.getSubsystem(secondConnectorHousing.getDescription());
				system.addRelation(new SimulinkExternRelation(system.getBlock(name).getOutPort(0), secondConnectorHousing.getDescription(), secondSubsystem.getConnectionPath(secondConnectorOccurrence.getLargeId()), system, 0));
			}
		}
		*/
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
    
    private ConnectorOccurrence findConnectorOccurrenceWithContactPoint(List<ConnectorOccurrence> connectorOccurrences, String id) {
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
    
    private Integer findPinNumWithContactPointId(ConnectorOccurrence connectorOccurrence, ConnectorHousing connectorHousing, String id) {
        List <ContactPoint> contactPoints = connectorOccurrence.getContactPoints();
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
        cavityInConnectorOccurrence = findCavityInConnectorOccurrence(connectorOccurrence, contactPoint.getContactedCavity());
		cavityInConnectorHousing = findCavityInConnectorHousing(connectorHousing, cavityInConnectorOccurrence.getPart());
		if (cavityInConnectorHousing != null) {
			return cavityInConnectorHousing.getCavityNumber();
		} else {
			return null;
		}
    }
    
    private Cavity findCavityInConnectorOccurrence(ConnectorOccurrence connectorOccurrence, String id) {
        List <Cavity> cavities = connectorOccurrence.getSlots().getCavities();
        for (Cavity cavity : cavities) {
			if (cavity.getId().equals(id)) {
				return cavity;
			}
		}
        return null; // If no corresponding CartesianPoint is found
    }
    
    private Cavity findCavityInConnectorHousing(ConnectorHousing connectorHousing, String id) {
        List <Cavity> cavities = connectorHousing.getSlots().getCavities();
        for (Cavity cavity : cavities) {
			if (cavity.getId().equals(id)) {
				return cavity;
			}
		}
        return null; // If no corresponding CartesianPoint is found
    }
    
    private ConnectorOccurrence findConnectorOccurrence(List<ConnectorOccurrence> connectorOccurrences, String id) {
        for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			if (connectorOccurrence.getId().equals(id)) {
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
    
    private GeneralWireOccurrence findGeneralWireOccurrence(List<GeneralWireOccurrence> generalWireOccurrences, String id) {
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
}
