package preevisiontosimulink.generator;

import java.util.List;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.*;

public class ModelGenerator {
    public static void generateModel(String modelName) {
        SimulinkSystem system = new SimulinkSystem(modelName);
        /*
        matlab.eval("add_block('simulink/Sources/Sine Wave', '" + modelName + "/Sine')");
        matlab.eval("add_block('simulink/Math Operations/Gain', '" + modelName + "/Gain')");
        matlab.eval("add_block('simulink/Commonly Used Blocks/Scope', '" + modelName + "/Scope')");
        matlab.eval("add_line('" + modelName + "', 'Sine/1', 'Gain/1')");
        matlab.eval("add_line('" + modelName + "', 'Gain/1', 'Scope/1')");
        matlab.eval("set_param('" + modelName + "/Gain', 'Gain', '2')");
        */
		
		system.addBlock(new DCVoltageSource(system, null));
		getLastBlock(system.getBlockList()).setParameter("v0", 10);
		system.addBlock(new Resistor(system, null));
		getLastBlock(system.getBlockList()).setParameter("R", 50);
		system.addRelation(new SimulinkRelation(getSecondLastBlock(system.getBlockList()).getOutputs().get(0), getLastBlock(system.getBlockList()).getInputs().get(0), system));
		
		system.addBlock(new ElectricalReference(system, null));
		system.addRelation(new SimulinkRelation(getSecondLastBlock(system.getBlockList()).getOutputs().get(0), getLastBlock(system.getBlockList()).getInputs().get(0), system));			
				
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
    
	public static ISimulinkBlock getThirdLastBlock(List<ISimulinkBlock> blockList) {
		if (blockList.isEmpty()) {
			return null; // Liste ist leer, gib null zurück
		}
		return blockList.get(blockList.size() - 3);
	}
}
