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
        system.addBlock(new SineWaveBlock(system)); 
        getLastBlock(system.getBlockList()).setParameter("Amplitude", 5); 
        getLastBlock(system.getBlockList()).setParameter("Bias", 2);
        system.addBlock(new GainBlock(system));
        system.getBlock("Gain" + 1).setParameter("Gain", 3);  
        system.addRelation(new SimulinkRelation(system.getBlock("SineWave1").getOutputs().get(0), system.getBlock("Gain1").getInputs().get(0), system));
        system.addBlock(new ScopeBlock(system));
		system.addRelation(new SimulinkRelation(system.getBlock("Gain1").getOutputs().get(0), system.getBlock("Scope1").getInputs().get(0), system));
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
}
