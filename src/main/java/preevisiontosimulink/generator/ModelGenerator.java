package preevisiontosimulink.generator;

import java.util.List;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.*;

public class ModelGenerator {
    public static void generateModel(String modelName) {
        SimulinkSystem system = new SimulinkSystem(modelName);
        SimulinkSubsystem lastSubsystem;
        SimulinkSubsystem secondLastSubsystem;
        /*
        matlab.eval("add_block('simulink/Sources/Sine Wave', '" + modelName + "/Sine')");
        matlab.eval("add_block('simulink/Math Operations/Gain', '" + modelName + "/Gain')");
        matlab.eval("add_block('simulink/Commonly Used Blocks/Scope', '" + modelName + "/Scope')");
        matlab.eval("add_line('" + modelName + "', 'Sine/1', 'Gain/1')");
        matlab.eval("add_line('" + modelName + "', 'Gain/1', 'Scope/1')");
        matlab.eval("set_param('" + modelName + "/Gain', 'Gain', '2')");
        
        system.addSubsystem(new SimulinkSubsystem(system, null));
        lastSubsystem = getLastSubsystem(system.getSubsystemList());
        lastSubsystem.addBlock(new Resistor(lastSubsystem, null));
        getLastBlock(lastSubsystem.getBlockList()).setParameter("R", 10);
		*/
        
		system.addBlock(new DCVoltageSource(system, null));	
		system.addBlock(new Resistor(system, null));
		system.addBlock(new Resistor(system, null));
		
		system.getBlock("DCVoltageSource0").setParameter("v0", 10);
		system.getBlock("Resistor0").setParameter("R", 50);
		system.getBlock("Resistor1").setParameter("R", 10);
		
		system.addRelation(new SimulinkRelation(system.getBlock("DCVoltageSource0"), system.getBlock("Resistor0"), 1, 1, system));
		system.addRelation(new SimulinkRelation(system.getBlock("Resistor0"), system.getBlock("Resistor1"), 1, 1, system));	
		system.addRelation(new SimulinkRelation(system.getBlock("Resistor0"), system.getBlock("Resistor1"), 2, 2, system));
		system.addRelation(new SimulinkRelation(system.getBlock("Resistor0"), system.getBlock("DCVoltageSource0"), 2, 2, system));
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
