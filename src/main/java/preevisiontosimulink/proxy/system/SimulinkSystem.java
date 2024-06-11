package preevisiontosimulink.proxy.system;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mathworks.engine.*;

import preevisiontosimulink.library.DCCurrentSource;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.library.VoltageSensor;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.port.Contact;
import preevisiontosimulink.proxy.relation.ISimulinkRelation;

public class SimulinkSystem implements ISimulinkSystem {
	private ISimulinkSystem parent = null;
	private String name;
	private List<ISimulinkBlock> blockList = new ArrayList<>();
	private List<ISimulinkRelation> relationList = new ArrayList<>();
	private List<SimulinkSubsystem> subsystemList = new ArrayList<>();

	public SimulinkSystem(String name) {
		this.name = name;
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

	public void generateModel() {
		try {
			// Start the MATLAB engine
			MatlabEngine matlab = MatlabEngine.startMatlab();
			// Generate the Simulink model (example)
			matlab.eval(name + " = new_system('" + name + "', 'Model')");

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

			// Generate the Simulink model for each relation in the relationList
			for (ISimulinkRelation relation : relationList) {
				relation.generateModel(matlab);
			}

			matlab.eval("Simulink.BlockDiagram.arrangeSystem('" + name + "')");
			
			for (SimulinkSubsystem subsystem : getSubsystemList(SubsystemType.KABEL)) {
				Contact leftContactPoint = subsystem.getLeftContactPoint();
				Contact rightContactPoint = subsystem.getRightContactPoint();
				System.out.println("Contact Point: " + leftContactPoint + ", " + rightContactPoint);
			}

			// Save the model
			String modelFilePath = "" + name + ".slx";
			matlab.eval("save_system('" + name + "', '" + modelFilePath + "')");

			System.out.println("Simulink model generated: " + name);

			// Close the MATLAB engine
			matlab.close();
		} catch (Exception e) {
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
	public List<SimulinkSubsystem> getSubsystemList(SubsystemType type) {
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
        for (SimulinkSubsystem subsystem : getSubsystemList(SubsystemType.KABEL)) {
            if (subsystem.getLeftContactPoint() != null && subsystem.getRightContactPoint() != null) {
                if (subsystem.getLeftContactPoint().equals(leftContactPoint) && subsystem.getRightContactPoint().equals(rightContactPoint)) {
                    return subsystem;
                } else if (subsystem.getLeftContactPoint().equals(rightContactPoint) && subsystem.getRightContactPoint().equals(leftContactPoint)) {
					return subsystem;
				}
            }
        }
        return null; // Return null if no matching subsystem is found
    }
}
