package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;
import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;


public class Inport extends SimulinkBlock {
    private static int num = 1;

    public Inport(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

    @Override
    public void initialize() {
    	this.BLOCK_NAME = "In";
    	this.BLOCK_PATH = "simulink/Commonly Used Blocks/In1";
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	}
		num++;
		
        // Initialize inputs and outputs if necessary
        this.inPorts.add(new SimulinkPort("1", this)); 
    }
}


