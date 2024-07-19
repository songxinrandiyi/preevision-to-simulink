package preevisiontosimulink.proxy.system;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.library.DCCurrentSource;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.library.VoltageSensor;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.relation.ISimulinkRelation;
import preevisiontosimulink.util.StringUtils;

public class SimulinkSystem implements ISimulinkSystem {
	private ISimulinkSystem parent = null;
	private String name;
	private SimulinkSystemType type;
	private String path = null;
	private List<ISimulinkBlock> blockList = new ArrayList<>();
	private List<ISimulinkRelation> relationList = new ArrayList<>();
	private List<SimulinkSubsystem> subsystemList = new ArrayList<>();

	public SimulinkSystem(String name, SimulinkSystemType type, String path) {
		this.name = name;
		this.type = type;
		this.path = path;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ISimulinkBlock addBlock(ISimulinkBlock block) {
		blockList.add(block);
		return block;
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
			// Start the MATLAB engine
			// matlab = MatlabEngine.startMatlab();
			// Generate the Simulink model (example)
			matlab.eval("new_system('" + name + "', 'Model')");

			// Generate the Simulink model for each subsystem in the subsystemList
			for (SimulinkSubsystem subsystem : subsystemList) {
				if (type == SimulinkSystemType.WIRING_HARNESS) {
					if (subsystem.getContactPoints() != null && subsystem.getContactPoints().size() > 0) {
						subsystem.generateModel(matlab);
					}
				} else {
					subsystem.generateModel(matlab);
				}			
			}

			// Generate the Simulink model for each block in the blockList
			for (ISimulinkBlock block : blockList) {
				block.generateModel(matlab);
			}

			// Generate the Simulink model for each relation in the relationList
			for (ISimulinkRelation relation : relationList) {
				relation.generateModel(matlab);
			}

			if (this.type != null) {
				if (this.type == SimulinkSystemType.WIRING_HARNESS) {
					logInfo();
				}
			}

			matlab.eval("Simulink.BlockDiagram.arrangeSystem('" + name + "')");

			matlab.eval("set_param('" + name + "', 'StopTime', '400')");

			String modelForderPath = "simulink/";
			if (path != null) {
				modelForderPath += path + "/";
			}

			Path simulinkDir = Paths.get(modelForderPath);
			if (!Files.exists(simulinkDir)) {
				Files.createDirectory(simulinkDir);
			}

			// Save the model
			String modelFilePath = modelForderPath + name + ".slx";
			matlab.eval("save_system('" + name + "', '" + modelFilePath + "')");

			System.out.println("Simulink model generated: " + name);

			// Close the MATLAB engine
			// matlab.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logInfo() {
		// Get the first part of the system's name
		String firstPartOfSystemName = StringUtils.getFirstPart(this.name);
		String logFileName = firstPartOfSystemName + "_log.txt";
		StringBuilder logContent = new StringBuilder();

		for (SimulinkSubsystem subsystem : getSubsystemList(SimulinkSubsystemType.STECKER)) {
			if (subsystem.getContactPoints() != null && subsystem.getContactPoints().size() > 0) {
				System.out.println("Connector: " + subsystem.getName());
				logContent.append("Connector: ").append(subsystem.getName()).append("\n");

				for (int i = 1; i < subsystem.getNumOfPins() + 1; i++) {
					System.out.println("Pin " + i + " has number of contact points: "
							+ subsystem.getContactsByPinNumber(i).size());
					logContent.append("Pin ").append(i).append(" has number of contact points: ")
							.append(subsystem.getContactsByPinNumber(i).size()).append("\n");

					for (Contact contact : subsystem.getContactsByPinNumber(i)) {
						System.out.println(contact.getName() + ", Pin " + contact.getPinNumberTo());
						logContent.append(contact.getName()).append(", Pin ").append(contact.getPinNumberTo())
								.append("\n");
					}
					System.out.println();
					logContent.append("\n");
				}
				System.out.println();
				System.out.println();
				logContent.append("\n\n");
			}
		}

		// Write to file, overwriting if it exists
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, false))) {
			writer.write(logContent.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ISimulinkBlock> getBlockList() {
		return blockList;
	}

	@Override
	public List<ISimulinkRelation> getRelationList() {
		return relationList;
	}

	@Override
	public SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem) {
		subsystemList.add(subsystem);
		return subsystem;
	}

	@Override
	public ISimulinkSystem getParent() {
		return parent;
	}

	@Override
	public List<SimulinkSubsystem> getSubsystemList(SimulinkSubsystemType type) {
		return subsystemList.stream().filter(subsystem -> subsystem.getType() == type).collect(Collectors.toList());
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
	public List<SimulinkSubsystem> getSubsystemsContainingString(String searchString) {
		return subsystemList.stream().filter(subsystem -> subsystem.getName().contains(searchString))
				.collect(Collectors.toList());
	}

	public SimulinkSubsystem findSubsystemWithContactPoints(Contact leftContactPoint, Contact rightContactPoint) {
		for (SimulinkSubsystem subsystem : getSubsystemList(SimulinkSubsystemType.KABEL)) {
			if (subsystem.getContactPoints().size() == 2) {
				if (subsystem.getContactPoints().get(0).equals(leftContactPoint)
						&& subsystem.getContactPoints().get(1).equals(rightContactPoint)) {
					return subsystem;
				} else if (subsystem.getContactPoints().get(0).equals(rightContactPoint)
						&& subsystem.getContactPoints().get(1).equals(leftContactPoint)) {
					return subsystem;
				}
			}
		}
		return null; // Return null if no matching subsystem is found
	}
}
