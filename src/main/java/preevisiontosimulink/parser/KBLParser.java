package preevisiontosimulink.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import preevisiontosimulink.library.CurrentSensor;
import preevisiontosimulink.library.ElectricalReference;
import preevisiontosimulink.library.LConnection;
import preevisiontosimulink.library.PSSimulinkConverter;
import preevisiontosimulink.library.RConnection;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.library.Scope;
import preevisiontosimulink.library.VoltageSensor;
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
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.relation.SimulinkSubToSubRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;
import preevisiontosimulink.proxy.system.SubsystemType;
import preevisiontosimulink.util.CalculatorUtils;
import preevisiontosimulink.util.ExcelGenerator;
import preevisiontosimulink.util.JAXBUtils;
import preevisiontosimulink.util.KBLModifier;
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
		system = new SimulinkSystem(modelName);

		generateBlocksAndConnections();
		
		system.generateModel();
	}
	
	public void generateExcel() {
	    system = new SimulinkSystem(modelName);

	    generateBlocksAndConnections();

	    ExcelGenerator.generateExcel(modelName, system);
	}
	
	public void generateUpdatedExcel() {
		ExcelGenerator.generateUpdatedExcel(modelName, system);
	}
	
	public void generateModifiedKBL() {	
		system = new SimulinkSystem(modelName);

		generateBlocksAndConnections();
	    
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

	private void generateBlocksAndConnections() {
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			ConnectorHousing connectorHousing = JAXBUtils.findConnectorHousing(connectorHousings, connectorOccurrence.getPart());

			if (system.getSubsystem(connectorOccurrence.getLargeId()) == null) {
				system.addSubsystem(
						new SimulinkSubsystem(system, connectorOccurrence.getLargeId(), SubsystemType.STECKER));
				List<Cavity> cavities = connectorOccurrence.getSlots().getCavities();
				for (Cavity cavity : cavities) {
					Integer cavityNumber = JAXBUtils.getCavityNumberById(connectorHousing, cavity.getPart());

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
					subsystem.addBlock(new Scope(subsystem, inPort.getName() + "_Display"));

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
					generalWireOccurrence = JAXBUtils.findGeneralWireOccurrence(generalWireOccurrences,
							connection.getWire());
				} 
				
				if (generalWireOccurrence != null && generalWireOccurrence.getLengthInformation() != null) {
					length = generalWireOccurrence.getLengthInformation().get(0).getLengthValue().getValueComponent();
					GeneralWire generalWire = JAXBUtils.findGeneralWire(generalWires, generalWireOccurrence.getPart());
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
					name = StringUtils.generateUniqueName(system, name);
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
					
					subsystem.addBlock(new VoltageSensor(subsystem, "U"));
					ISimulinkBlock voltageSensor = subsystem.getBlock("U");

					subsystem.addBlock(new PSSimulinkConverter(subsystem, "PS"));
					ISimulinkBlock converter = subsystem.getBlock("PS");

					subsystem.addBlock(new Scope(subsystem, "Display"));
					ISimulinkBlock display = subsystem.getBlock("Display");

					subsystem.addRelation(
							new SimulinkRelation(inPort.getInPort(0), resistor.getInPort(0), subsystem));
					
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
					startConnectorOccurrence = JAXBUtils.findConnectorOccurrenceWithContactPoint(connectorOccurrences,
							startExtremity.getContactPoint());
					if (startConnectorOccurrence != null) {
						ConnectorHousing startConnectorHousing = JAXBUtils.findConnectorHousing(connectorHousings,
								startConnectorOccurrence.getPart());
						if (startConnectorHousing != null) {
							startPin = JAXBUtils.findPinNumWithContactPointId(startConnectorOccurrence, startConnectorHousing,
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
					
					
					endConnectorOccurrence = JAXBUtils.findConnectorOccurrenceWithContactPoint(connectorOccurrences,
							endExtremity.getContactPoint());
					if (endConnectorOccurrence != null) {
						ConnectorHousing endConnectorHousing = JAXBUtils.findConnectorHousing(connectorHousings,
								endConnectorOccurrence.getPart());
						if (endConnectorHousing != null) {

							endPin = JAXBUtils.findPinNumWithContactPointId(endConnectorOccurrence, endConnectorHousing,
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
											subsystem.getKblInformation().setWireNumber(generalWireOccurrence.getWireNumber());	
										}								
										if (generalWireOccurrence.getId() != null) {
											subsystem.getKblInformation().setGeneralWireOccurrenceId(generalWireOccurrence.getId());
										}
										if (generalWireOccurrence.getPart() != null) {
                                            subsystem.getKblInformation().setGeneralWireId(generalWireOccurrence.getPart());
                                        }
									}
									
									if (length != null) {
										subsystem.getKblInformation().setLength(length);								
									}
									if (crossSectionArea != null) {
										subsystem.getKblInformation().setCrossSectionArea(crossSectionArea);
									}
									if (connection.getSignalName() != null) {
										subsystem.getKblInformation().setSignalName(connection.getSignalName());
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
