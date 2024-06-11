package preevisiontosimulink.generator;

import java.util.List;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkExternRelation;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;

public class ModelGenerator {
	public static void generateModel(String modelName) {
		SimulinkSystem system = new SimulinkSystem(modelName);
		SimulinkSubsystem lastSubsystem;
		SimulinkSubsystem secondLastSubsystem;
		/*
		 * matlab.eval("add_block('simulink/Sources/Sine Wave', '" + modelName +
		 * "/Sine')"); matlab.eval("add_block('simulink/Math Operations/Gain', '" +
		 * modelName + "/Gain')");
		 * matlab.eval("add_block('simulink/Commonly Used Blocks/Scope', '" + modelName
		 * + "/Scope')"); matlab.eval("add_line('" + modelName +
		 * "', 'Sine/1', 'Gain/1')"); matlab.eval("add_line('" + modelName +
		 * "', 'Gain/1', 'Scope/1')"); matlab.eval("set_param('" + modelName +
		 * "/Gain', 'Gain', '2')");
		 * 
		 * system.addSubsystem(new SimulinkSubsystem(system, null)); lastSubsystem =
		 * getLastSubsystem(system.getSubsystemList()); lastSubsystem.addBlock(new
		 * Resistor(lastSubsystem, null));
		 * getLastBlock(lastSubsystem.getBlockList()).setParameter("R", 10);
		 */

		system.addBlock(new DCVoltageSource(system, null));
		system.addBlock(new Resistor(system, null));
		system.addBlock(new Resistor(system, null));
		system.addBlock(new ElectricalReference(system, "Ref1"));
		system.addBlock(new SolverConfiguration(system, "Solver1"));
		system.addSubsystem(new SimulinkSubsystem(system, null, null));
		lastSubsystem = system.getSubsystem("Subsystem1");
		lastSubsystem.addBlock(new Resistor(lastSubsystem, "Resistor1"));
		lastSubsystem.addBlock(new Resistor(lastSubsystem, "Resistor2"));
		lastSubsystem.addInConnection(new LConnection(lastSubsystem, "LConn1"));
		lastSubsystem.addOutConnection(new RConnection(lastSubsystem, "RConn1"));

		system.getBlock("DCVoltageSource1").setParameter("v0", 10);
		system.getBlock("Resistor1").setParameter("R", 50);
		system.getBlock("Resistor2").setParameter("R", 10);
		lastSubsystem.getBlock("Resistor1").setParameter("R", 20);
		lastSubsystem.getBlock("Resistor2").setParameter("R", 25);

		system.addRelation(new SimulinkRelation(system.getBlock("DCVoltageSource1").getInPort(0),
				system.getBlock("Resistor1").getInPort(0), system));
		system.addRelation(new SimulinkExternRelation(system.getBlock("Resistor1").getOutPort(0), "Subsystem1",
				"LConn1", system, 0));
		system.addRelation(new SimulinkExternRelation(system.getBlock("DCVoltageSource1").getOutPort(0), "Subsystem1",
				"RConn1", system, 0));
		system.addRelation(new SimulinkRelation(system.getBlock("Resistor1").getInPort(0),
				system.getBlock("Resistor2").getInPort(0), system));
		system.addRelation(new SimulinkRelation(system.getBlock("Resistor1").getOutPort(0),
				system.getBlock("Resistor2").getOutPort(0), system));
		system.addRelation(new SimulinkRelation(system.getBlock("Ref1").getInPort(0),
				system.getBlock("DCVoltageSource1").getOutPort(0), system));
		system.addRelation(new SimulinkRelation(system.getBlock("Solver1").getInPort(0),
				system.getBlock("Ref1").getInPort(0), system));

		system.addRelation(new SimulinkRelation(lastSubsystem.getInConnection("LConn1").getInPort(0),
				lastSubsystem.getBlock("Resistor1").getInPort(0), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getBlock("Resistor1").getOutPort(0),
				lastSubsystem.getOutConnection("RConn1").getInPort(0), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getBlock("Resistor1").getInPort(0),
				lastSubsystem.getBlock("Resistor2").getInPort(0), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getBlock("Resistor1").getOutPort(0),
				lastSubsystem.getBlock("Resistor2").getOutPort(0), lastSubsystem));

		system.generateModel();
	}

	public static ISimulinkBlock getLastBlock(List<ISimulinkBlock> blockList) {
		if (blockList.isEmpty()) {
			return null; // Liste ist leer, gib null zurück
		}
		return blockList.get(blockList.size() - 1);
	}

	public static ISimulinkBlock getSecondLastBlock(List<ISimulinkBlock> blockList) {
		if (blockList.isEmpty()) {
			return null; // Liste ist leer, gib null zurück
		}
		return blockList.get(blockList.size() - 2);
	}

	public static SimulinkSubsystem getLastSubsystem(List<SimulinkSubsystem> subsystemList) {
		if (subsystemList.isEmpty()) {
			return null; // Liste ist leer, gib null zurück
		}
		return subsystemList.get(subsystemList.size() - 1);
	}

	public static SimulinkSubsystem getSecondLastSubsystem(List<SimulinkSubsystem> subsystemList) {
		if (subsystemList.isEmpty()) {
			return null; // Liste ist leer, gib null zurück
		}
		return subsystemList.get(subsystemList.size() - 2);
	}
}
