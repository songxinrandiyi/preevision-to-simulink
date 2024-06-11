package preevisiontosimulink.proxy.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mathworks.engine.*;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.relation.ISimulinkRelation;

public class SimulinkSubsystem implements ISimulinkSystem {
	private static final String BLOCK_NAME = "Subsystem";
	private static final String BLOCK_PATH = "simulink/Ports & Subsystems/Subsystem";
	private ISimulinkSystem parent;
	private String name;
	private static int num = 1;
	private SubsystemType type;

	private List<LConnection> inConnections = new ArrayList<>();
	private List<RConnection> outConnections = new ArrayList<>();
	private List<Inport> inPorts = new ArrayList<>();
	private List<Outport> outPorts = new ArrayList<>();
	private List<ISimulinkBlock> blockList = new ArrayList<>();
	private List<ISimulinkRelation> relationList = new ArrayList<>();
	private List<SimulinkSubsystem> subsystemList = new ArrayList<>();

	private Contact leftContactPoint = null;
	private Contact rightContactPoint = null;

	public Contact getLeftContactPoint() {
		return leftContactPoint;
	}

	public void setLeftContactPoint(Contact leftContactPoint) {
		this.leftContactPoint = leftContactPoint;
	}

	public Contact getRightContactPoint() {
		return rightContactPoint;
	}

	public void setRightContactPoint(Contact rightContactPoint) {
		this.rightContactPoint = rightContactPoint;
	}

	public SimulinkSubsystem(ISimulinkSystem parent, String name, SubsystemType type) {
		this.parent = parent;
		if (name == null) {
			this.name = BLOCK_NAME + num;
		} else {
			this.name = name;
		}
		num++;
		this.type = type != null ? type : SubsystemType.STECKER;
	}

	public ISimulinkSystem getParent() {
		return parent;
	}

	public Inport addInPort(Inport port) {
		inPorts.add(port);
		return port;
	}

	public Inport getInPort(int index) {
		return inPorts.get(index);
	}

	public Inport getInPort(String name) {
		for (Inport port : inPorts) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	public int getInPortIndex(String name) {
		for (int i = 0; i < inPorts.size(); i++) {
			if (inPorts.get(i).getName().equals(name)) {
				return i + 1;
			}
		}
		// If the port with the given name is not found, return -1 or handle it as
		// needed
		return -1; // or throw new IllegalArgumentException("Input port not found: " + name);
	}

	public List<Inport> getInPorts() {
		return inPorts;
	}

	public Outport addOutPort(Outport port) {
		outPorts.add(port);
		return port;
	}

	public Outport getOutPort(int index) {
		return outPorts.get(index);
	}

	public Outport getOutPort(String name) {
		for (Outport port : outPorts) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	public int getOutPortIndex(String name) {
		for (int i = 0; i < outPorts.size(); i++) {
			if (outPorts.get(i).getName().equals(name)) {
				return i + 1;
			}
		}
		// If the port with the given name is not found, return -1 or handle it as
		// needed
		return -1; // or throw new IllegalArgumentException("Input port not found: " + name);
	}

	public List<Outport> getOutPorts() {
		return outPorts;
	}

	public LConnection addInConnection(LConnection port) {
		inConnections.add(port);
		return port;
	}

	public LConnection getInConnection(int index) {
		return inConnections.get(index);
	}

	public LConnection getInConnection(String name) {
		for (LConnection port : inConnections) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	public int getInConnectionIndex(String name) {
		for (int i = 0; i < inConnections.size(); i++) {
			if (inConnections.get(i).getName().equals(name)) {
				return i + 1;
			}
		}
		// If the port with the given name is not found, return -1 or handle it as
		// needed
		return -1; // or throw new IllegalArgumentException("Input port not found: " + name);
	}

	public List<LConnection> getInConnections() {
		return inConnections;
	}

	public RConnection addOutConnection(RConnection port) {
		outConnections.add(port);
		return port;
	}

	public RConnection getOutConnection(int index) {
		return outConnections.get(index);
	}

	public RConnection getOutConnection(String name) {
		for (RConnection port : outConnections) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	public int getOutConnectionIndex(String name) {
		for (int i = 0; i < outConnections.size(); i++) {
			if (outConnections.get(i).getName().equals(name)) {
				return i + 1;
			}
		}
		// If the port with the given name is not found, return -1 or handle it as
		// needed
		return -1; // or throw new IllegalArgumentException("Output port not found: " + name);
	}

	public List<RConnection> getOutConnections() {
		return outConnections;
	}

	public String getConnectionPath(String name) {
		for (int i = 0; i < inConnections.size(); i++) {
			if (inConnections.get(i).getName().equals(name)) {
				int n = i + 1;
				return "LConn" + n;
			}
		}
		for (int i = 0; i < outConnections.size(); i++) {
			if (outConnections.get(i).getName().equals(name)) {
				int n = i + 1;
				return "RConn" + n;
			}
		}
		return null;
	}

	public String getPortPath(String name) {
		for (int i = 0; i < inPorts.size(); i++) {
			if (inPorts.get(i).getName().equals(name)) {
				int n = i + 1;
				return "" + n;
			}
		}
		for (int i = 0; i < outPorts.size(); i++) {
			if (outPorts.get(i).getName().equals(name)) {
				int n = i + 1;
				return "" + n;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ISimulinkRelation addRelation(ISimulinkRelation relation) {
		relationList.add(relation);
		return relation;
	}

	@Override
	public ISimulinkBlock getBlock(String name) {
		for (ISimulinkBlock block : blockList) {
			if (block.getName().equals(name)) {
				return block;
			}
		}
		return null;
	}

	public void generateModel(MatlabEngine matlab) {
		try {
			String combinedPath = generateCombinedPath();

			matlab.eval("add_block('" + BLOCK_PATH + "', '" + combinedPath + "')");
			matlab.eval("delete_line('" + combinedPath + "','In1/1','Out1/1')");
			matlab.eval("delete_block('" + combinedPath + "/In1')");
			matlab.eval("delete_block('" + combinedPath + "/Out1')");

			// Generate the Simulink model for each subsystem in the subsystemList
			for (SimulinkSubsystem subsystem : subsystemList) {
				subsystem.generateModel(matlab);
			}

			for (DCCurrentSource currentSource : getAllCurrentSourceBlocks()) {
				currentSource.setParameter("Orientation", "Left");
			}

			for (VoltageSensor voltageSensor : getAllVoltageSensorBlocks()) {
				voltageSensor.setParameter("Orientation", "Right");
			}

			// Generate the Simulink model for each block in the blockList
			for (ISimulinkBlock block : blockList) {
				block.generateModel(matlab);
			}

			// Generate the Simulink model for each LConnection in the inPorts
			for (LConnection port : inConnections) {
				port.generateModel(matlab);
			}

			// Generate the Simulink model for each RConnection in the outPorts
			for (RConnection port : outConnections) {
				port.generateModel(matlab);
			}

			// Generate the Simulink model for each port in the inPorts
			for (Inport port : inPorts) {
				port.generateModel(matlab);
			}

			// Generate the Simulink model for each port in the outPorts
			for (Outport port : outPorts) {
				port.generateModel(matlab);
			}

			if (this.type == SubsystemType.STECKER) {
				if (inConnections != null && inConnections.size() > 0) {
					matlab.eval("h = getSimulinkBlockHandle('" + combinedPath + "/" + inConnections.get(0).getName()
							+ "')");
					matlab.eval("pos = get_param(h,'Position')");
					matlab.eval("pos1 = [pos(1)+100 pos(2)-15 pos(3)+100 pos(4)]");
					matlab.eval("pos2 = [pos1(1)+70 pos1(2)-70 pos1(3)+70 pos1(4)-70]");
					matlab.eval("pos3 = [pos2(1)+100 pos2(2)-15 pos2(3)+150 pos2(4)]");
					matlab.eval("set_param('" + combinedPath + "/" + inConnections.get(0).getName()
							+ "_I', 'Position', pos1)");
					matlab.eval("set_param('" + combinedPath + "/" + inConnections.get(0).getName()
							+ "_PS', 'Position', pos2)");
					matlab.eval("set_param('" + combinedPath + "/" + inConnections.get(0).getName()
							+ "_Display', 'Position', pos3)");

					for (int i = 1; i < inConnections.size(); i++) {
						LConnection port = inConnections.get(i);
						matlab.eval("pos = [pos(1) pos(2)+200 pos(3) pos(4)+200]");
						matlab.eval("pos1 = [pos(1)+100 pos(2)-15 pos(3)+100 pos(4)]");
						matlab.eval("pos2 = [pos1(1)+70 pos1(2)-70 pos1(3)+70 pos1(4)-70]");
						matlab.eval("pos3 = [pos2(1)+100 pos2(2)-15 pos2(3)+150 pos2(4)]");
						matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "', 'Position', pos)");
						matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "_I', 'Position', pos1)");
						matlab.eval("set_param('" + combinedPath + "/" + port.getName() + "_PS', 'Position', pos2)");
						matlab.eval(
								"set_param('" + combinedPath + "/" + port.getName() + "_Display', 'Position', pos3)");
					}

					if (getBlock(name + "_E") != null) {
						matlab.eval("pos = [pos(1)+700 pos(2)+100 pos(3)+700 pos(4)+100]");
						matlab.eval("set_param('" + combinedPath + "/" + name + "_E', 'Position', pos)");
					}
				}
			}

			if (this.type == SubsystemType.KABEL) {
				LConnection inPort = inConnections.get(0);
				RConnection outPort = outConnections.get(0);

				matlab.eval("h = getSimulinkBlockHandle('" + combinedPath + "/" + inPort.getName() + "')");
				matlab.eval("pos = get_param(h,'Position')");
				matlab.eval("pos1 = [pos(1)+700 pos(2) pos(3)+700 pos(4)]");
				matlab.eval("pos2 = [pos(1)+100 pos(2)-12 pos(3)+115 pos(4)+12]");
				matlab.eval("pos3 = [pos(1)+200 pos(2) pos(3)+200 pos(4)]");
				matlab.eval("pos4 = [pos3(1) pos3(2)-100 pos3(3) pos3(4)-80]");
				matlab.eval("pos5 = [pos4(1)+100 pos4(2)+10 pos4(3)+100 pos4(4)-10]");
				matlab.eval("pos6 = [pos5(1)+100 pos5(2)-10 pos5(3)+200 pos5(4)+10]");

				matlab.eval("set_param('" + combinedPath + "/" + outPort.getName() + "', 'Position', pos1)");
				matlab.eval("set_param('" + combinedPath + "/" + "I', 'Position', pos2)");
				matlab.eval("set_param('" + combinedPath + "/" + "R', 'Position', pos3)");
				matlab.eval("set_param('" + combinedPath + "/" + "U', 'Position', pos4)");
				matlab.eval("set_param('" + combinedPath + "/" + "PS', 'Position', pos5)");
				matlab.eval("set_param('" + combinedPath + "/" + "Display', 'Position', pos6)");
			}

			// Generate the Simulink model for each relation in the relationList
			for (ISimulinkRelation relation : relationList) {
				relation.generateModel(matlab);
			}

			matlab.eval("Simulink.BlockDiagram.arrangeSystem('" + combinedPath + "')");

			System.out.println("Simulink subsystem generated: " + combinedPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SubsystemType getType() {
		return type;
	}

	public void setType(SubsystemType type) {
		this.type = type;
	}

	@Override
	public List<ISimulinkBlock> getBlockList() {
		return blockList;
	}

	@Override
	public List<ISimulinkRelation> getRelationList() {
		return relationList;
	}

	public SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem) {
		subsystemList.add(subsystem);
		return subsystem;
	}

	public String generateCombinedPath() {
		StringBuilder pathBuilder = new StringBuilder(name);
		ISimulinkSystem currentParent = parent;
		while (currentParent != null) {
			pathBuilder.insert(0, currentParent.getName() + "/");
			currentParent = currentParent.getParent();
		}
		return pathBuilder.toString();
	}

	@Override
	public ISimulinkBlock addBlock(ISimulinkBlock block) {
		blockList.add(block);
		return block;
	}

	@Override
	public SimulinkSubsystem getSubsystem(String name) {
		for (SimulinkSubsystem subsystem : subsystemList) {
			if (subsystem.getName().equals(name)) {
				return subsystem;
			}
		}
		return null;
	}

	@Override
	public ISimulinkRelation getRelation(String name) {
		for (ISimulinkRelation relation : relationList) {
			if (relation.getName().equals(name)) {
				return relation;
			}
		}
		return null;
	}

	private int extractNumber(String name) {
		String[] parts = name.split("_");
		return Integer.parseInt(parts[0]);
	}

	public void reorderConnectionsForExcel() {
		// Reorder inConnections
		Collections.sort(inConnections, new Comparator<LConnection>() {
			@Override
			public int compare(LConnection c1, LConnection c2) {
				return extractNumber(c1.getName()) - extractNumber(c2.getName());
			}
		});

		// Reorder outConnections
		Collections.sort(outConnections, new Comparator<RConnection>() {
			@Override
			public int compare(RConnection c1, RConnection c2) {
				return extractNumber(c1.getName()) - extractNumber(c2.getName());
			}
		});
	}

	public void reorderConnectionsRecursivelyForExcel(SimulinkSubsystem subsystem) {
		// Reorder connections for the current subsystem
		subsystem.reorderConnectionsForExcel();

		// Reorder connections for each child subsystem
		for (SimulinkSubsystem childSubsystem : subsystem.getSubsystemList(SubsystemType.KABEL)) {
			reorderConnectionsRecursivelyForExcel(childSubsystem);
		}
	}

	public void reorderConnectionsForKBL() {
		// Reorder inConnections
		Collections.sort(inConnections, new Comparator<LConnection>() {
			@Override
			public int compare(LConnection c1, LConnection c2) {
				return convertStringToInt(c1.getName()) - convertStringToInt(c2.getName());
			}
		});

		// Reorder outConnections
		Collections.sort(outConnections, new Comparator<RConnection>() {
			@Override
			public int compare(RConnection c1, RConnection c2) {
				return convertStringToInt(c1.getName()) - convertStringToInt(c2.getName());
			}
		});
	}

	public void reorderConnectionsRecursivelyForKBL(SimulinkSubsystem subsystem) {
		// Reorder connections for the current subsystem
		subsystem.reorderConnectionsForKBL();

		// Reorder connections for each child subsystem
		for (SimulinkSubsystem childSubsystem : subsystem.getSubsystemList(SubsystemType.KABEL)) {
			reorderConnectionsRecursivelyForKBL(childSubsystem);
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

	@Override
	public List<Resistor> getAllResistorBlocks() {
		return blockList.stream().filter(block -> block instanceof Resistor).map(block -> (Resistor) block)
				.collect(Collectors.toList());
	}

	@Override
	public List<DCCurrentSource> getAllCurrentSourceBlocks() {
		return blockList.stream().filter(block -> block instanceof DCCurrentSource)
				.map(block -> (DCCurrentSource) block).collect(Collectors.toList());
	}

	@Override
	public List<VoltageSensor> getAllVoltageSensorBlocks() {
		return blockList.stream().filter(block -> block instanceof VoltageSensor).map(block -> (VoltageSensor) block)
				.collect(Collectors.toList());
	}

	@Override
	public List<SimulinkSubsystem> getSubsystemList(SubsystemType type) {
		return subsystemList.stream().filter(subsystem -> subsystem.getType() == type).collect(Collectors.toList());
	}

	@Override
	public List<SimulinkSubsystem> getSubsystemsContainingString(String searchString) {
		return subsystemList.stream().filter(subsystem -> subsystem.getName().contains(searchString))
				.collect(Collectors.toList());
	}
}
