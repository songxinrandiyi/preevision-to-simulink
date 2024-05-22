package preevisiontosimulink.generator;

import java.util.List;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.SimulinkExternRelation;
import preevisiontosimulink.proxy.relation.SimulinkRelation;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;
import preevisiontosimulink.proxy.system.SimulinkSystem;

public class ModelGenerator2 {
    public static void main(String[] args) {
		generateModel("Hallo");
    }
	
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
        
		system.addBlock(new SineWave(system, null));	
		system.addBlock(new SineWave(system, null));	
		system.addBlock(new Scope(system, null));	
		system.addSubsystem(new SimulinkSubsystem(system, null));
		lastSubsystem = system.getSubsystem("Subsystem1");
		lastSubsystem.addBlock(new Gain(lastSubsystem, "Gain1"));
		lastSubsystem.addBlock(new Gain(lastSubsystem, "Gain2"));
		lastSubsystem.addBlock(new Add(lastSubsystem, "Add1"));
		lastSubsystem.addInPort(new Inport(lastSubsystem, "Inport1"));
		lastSubsystem.addInPort(new Inport(lastSubsystem, "Inport2"));
		lastSubsystem.addOutPort(new Outport(lastSubsystem, "Outport1"));
		
		
		system.getBlock("Sine1").setParameter("Amplitude", 10);
		system.getBlock("Sine2").setParameter("Amplitude", 5);
		system.getBlock("Sine2").setParameter("Phase", Math.PI/2);
		lastSubsystem.getBlock("Gain1").setParameter("Gain", 10);
		lastSubsystem.getBlock("Gain2").setParameter("Gain", 10);
		lastSubsystem.getBlock("Add1").setParameter("Inputs", "+-");
		
		system.addRelation(new SimulinkExternRelation(system.getBlock("Sine1").getOutPort(0), "Subsystem1", lastSubsystem.getPortPath("Inport1") ,system, 0));
		system.addRelation(new SimulinkExternRelation(system.getBlock("Sine2").getOutPort(0), "Subsystem1", lastSubsystem.getPortPath("Inport2") ,system, 0));
		system.addRelation(new SimulinkExternRelation(system.getBlock("Scope1").getInPort(0), "Subsystem1", lastSubsystem.getPortPath("Outport1") ,system, 1));
		
		system.addRelation(new SimulinkRelation(lastSubsystem.getInPort("Inport1").getOutPort(0), lastSubsystem.getBlock("Gain1").getInPort(0), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getInPort("Inport2").getOutPort(0), lastSubsystem.getBlock("Gain2").getInPort(0), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getBlock("Gain1").getOutPort(0), lastSubsystem.getBlock("Add1").getInPort(0), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getBlock("Gain2").getOutPort(0), lastSubsystem.getBlock("Add1").getInPort(1), lastSubsystem));
		system.addRelation(new SimulinkRelation(lastSubsystem.getBlock("Add1").getOutPort(0), lastSubsystem.getOutPort("Outport1").getInPort(0), lastSubsystem));
				
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
