package preevisiontosimulink.util;

import java.util.List;

import preevisiontosimulink.library.CurrentSensor;
import preevisiontosimulink.library.ElectricalReference;
import preevisiontosimulink.library.LConnection;
import preevisiontosimulink.library.PSSimulinkConverter;
import preevisiontosimulink.library.RConnection;
import preevisiontosimulink.library.Resistor;
import preevisiontosimulink.library.Scope;
import preevisiontosimulink.library.VoltageSensor;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;

public class SimulinkSubsystemInitHelper {
	
	public static void initStecker(SimulinkSubsystem subsystem) {		
		SimulinkSubsystemHelper.reorderConnections(subsystem);
		subsystem.addBlock(new ElectricalReference(subsystem, subsystem.getName() + "_E"));

		List<LConnection> inConnections = subsystem.getInConnections();
		if (inConnections != null) {
			for (LConnection inPort : inConnections) {
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
	
	public static void initThermalStecker(SimulinkSystem system) {
		
	}
	
	public static void initKabel(SimulinkSubsystem subsystem, Double resistance) {		
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

        subsystem.addRelation(new SimulinkRelation(inPort.getInPort(0), resistor.getInPort(0), subsystem));

        subsystem.addRelation(new SimulinkRelation(resistor.getOutPort(0), outPort.getInPort(0), subsystem));

        subsystem.addRelation(new SimulinkRelation(resistor.getOutPort(0), voltageSensor.getOutPort(1), subsystem));
        subsystem.addRelation(new SimulinkRelation(resistor.getInPort(0), voltageSensor.getInPort(0), subsystem));
        subsystem.addRelation(new SimulinkRelation(voltageSensor.getOutPort(0), converter.getInPort(0), subsystem));
        subsystem.addRelation(new SimulinkRelation(converter.getOutPort(0), display.getInPort(0), subsystem));
	}
	
	public static void initThermalKabel(SimulinkSubsystem subsystem) {		
		
	}
}
