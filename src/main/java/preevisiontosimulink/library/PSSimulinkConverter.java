package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;
import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.LConnectionPort;
import preevisiontosimulink.proxy.port.RConnectionPort;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;


public class PSSimulinkConverter extends SimulinkBlock {

    private static int num = 1;

    public PSSimulinkConverter(ISimulinkSystem parent, String name) {
		super(parent, name);
    }
    
    @Override
    public void initialize() {	
    	this.BLOCK_NAME = "PSConverter";
    	this.BLOCK_PATH = "nesl_utility/PS-Simulink Converter";
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	}
		num++;
        // Initialize inputs and outputs if necessary
		this.inPorts.add(new LConnectionPort(1, this));
        this.outPorts.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the block
        this.parameters.add(new SimulinkParameter<Integer>("NumInputPorts", this));
    }    
}

