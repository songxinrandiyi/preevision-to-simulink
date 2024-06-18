package preevisiontosimulink.generator;

import java.util.List;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkExternRelation;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;

public class ModelGenerator {
	public static void main(String[] args) {
		generateModel("simpleCircuit");
	}
	
	public static void generateModel(String modelName) {
		// Create the simulink system
		SimulinkSystem system = new SimulinkSystem(modelName);

		// Add blocks
		system.addBlock(new DCVoltageSource(system, "DC"));
		system.addBlock(new Resistor(system, "R1"));
		system.addBlock(new ElectricalReference(system, "Ref1"));
		system.addBlock(new SolverConfiguration(system, "Solver1"));

		// Set parameters
		system.getBlock("DC").setParameter("v0", 10);
		system.getBlock("R1").setParameter("R", 50);

		// Bind blocks
		system.addRelation(new SimulinkRelation(system.getBlock("DC").getInPort(0),
				system.getBlock("R1").getInPort(0), system));
		system.addRelation(new SimulinkRelation(system.getBlock("R1").getOutPort(0),
				system.getBlock("Ref1").getInPort(0), system));
		system.addRelation(new SimulinkRelation(system.getBlock("DC").getOutPort(0),
				system.getBlock("Ref1").getInPort(0), system));
		system.addRelation(new SimulinkRelation(system.getBlock("Solver1").getInPort(0),
				system.getBlock("Ref1").getInPort(0), system));

		system.generateModel();
	}
}
