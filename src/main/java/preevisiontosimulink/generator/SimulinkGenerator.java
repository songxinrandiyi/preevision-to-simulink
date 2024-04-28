package preevisiontosimulink.generator;

import java.util.List;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.*;

public class SimulinkGenerator {
    public static void generateModel() {
        SimulinkSystem system = new SimulinkSystem("Model");
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
        
        system.generateModel();
    }
    
    public static ISimulinkBlock getLastBlock(List<ISimulinkBlock> blockList) {
        if (blockList.isEmpty()) {
            return null; // Liste ist leer, gib null zurück
        }
        return blockList.get(blockList.size() - 1);
    }
}
