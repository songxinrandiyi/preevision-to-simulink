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

import com.mathworks.engine.MatlabEngine;

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
import preevisiontosimulink.util.KBLModifier;
import preevisiontosimulink.util.KBLUtils;
import preevisiontosimulink.util.SimulinkSubsystemInitHelper;
import preevisiontosimulink.util.StringUtils;

public class KBLParser {
	private SimulinkSystem system;
	private String modelName;
	private MatlabEngine matlab;
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
		startMatlabEngine();
		system = new SimulinkSystem(StringUtils.produceValidModelNameFromWire(modelName), 
				SimulinkSystemType.WIRING_HARNESS, null);
		for (ConnectorOccurrence connectorOccurrence : connectorOccurrences) {
			parsingConnectorOccurrences(connectorOccurrence);
			SimulinkSubsystemInitHelper.generateConnectorOccurrences(connectorOccurrence, 
					SimulinkSubsystemType.STECKER, system);
		}
		for (Connection connection : connections) {
			parsingConnection(connection);
		}
		for (Connection connection : connections) {
			if (connection.isValid()) {
				SimulinkSubsystemInitHelper.generateConnection(connection, system, SimulinkSubsystemType.KABEL);
			}
		}
		system.generateModel(matlab);
		closeMatlabEngine();
	}
	
	public void generateThermalModel(GeneralWire generalWire) {
		system = new SimulinkSystem(StringUtils.produceValidModelNameFromWire(generalWire.getPartNumber()), 
				SimulinkSystemType.THERMAL_SIMULATION, modelName);
		SimulinkSubsystemInitHelper.generateThermalConnector(generalWire, system);
		SimulinkSubsystemInitHelper.generateThermalCable(generalWire, system);
		system.generateModel(matlab);
	}
	
	public void startMatlabEngine() {
		try {
			matlab = MatlabEngine.startMatlab();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeMatlabEngine() {
		try {
			matlab.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getGeneralWireInformation() {
		for (Connection connection : connections) {
			parsingConnection(connection);
		}
	}

	public void generateModifiedKBL() {
		KBLModifier.generateModifiedKBL(kblFiles, getValidGeneralWires());
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

	private void parsingConnectorOccurrences(ConnectorOccurrence connectorOccurrence) {
		ConnectorHousing connectorHousing = null;
		if (connectorOccurrence.getPart() != null) {
			connectorHousing = KBLUtils.findConnectorHousing(connectorHousings, connectorOccurrence.getPart());
		}

		if (connectorHousing != null) {
			connectorOccurrence.setConnectorHousing(connectorHousing);
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
				generalWireOccurrence = KBLUtils.findGeneralWireOccurrence(generalWireOccurrences,
						connection.getWire());
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
				ConnectorOccurrence startConnectorOccurrence = KBLUtils.findConnectorOccurrenceWithContactPoint(
						connectorOccurrences, startExtremity.getContactPoint());
				if (startConnectorOccurrence != null) {
					connection.setStartConnector(startConnectorOccurrence);
					ConnectorHousing startConnectorHousing = KBLUtils.findConnectorHousing(connectorHousings,
							startConnectorOccurrence.getPart());

					if (startConnectorHousing != null) {
						startConnectorOccurrence.setConnectorHousing(startConnectorHousing);
						startPin = KBLUtils.findPinNumWithContactPointId(startConnectorOccurrence,
								startConnectorHousing, startExtremity.getContactPoint());

						if (startPin != null) {
							connection.setStartPin(startPin);
						}
					}
				}
				ConnectorOccurrence endConnectorOccurrence = KBLUtils
						.findConnectorOccurrenceWithContactPoint(connectorOccurrences, endExtremity.getContactPoint());
				if (endConnectorOccurrence != null) {
					connection.setEndConnector(endConnectorOccurrence);
					ConnectorHousing endConnectorHousing = KBLUtils.findConnectorHousing(connectorHousings,
							endConnectorOccurrence.getPart());
					if (endConnectorHousing != null) {
						endConnectorOccurrence.setConnectorHousing(endConnectorHousing);
						endPin = KBLUtils.findPinNumWithContactPointId(endConnectorOccurrence, endConnectorHousing,
								endExtremity.getContactPoint());

						if (endPin != null) {
							connection.setEndPin(endPin);
						}
					}
				}
			}
		}
	}

	public List<GeneralWire> getGeneralWires() {
		return generalWires;
	}

	public List<GeneralWire> getValidGeneralWires() {
		return generalWires.stream().filter(GeneralWire::isValid).collect(Collectors.toList());
	}
}
