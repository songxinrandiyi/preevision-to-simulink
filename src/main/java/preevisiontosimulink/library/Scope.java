package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;


public class Scope extends SimulinkBlock {

    private static int num = 0;

    public Scope(ISimulinkSystem parent, String name) {
		super(parent, name);
    }
    
    @Override
    public void initialize() {	
    	this.BLOCK_NAME = "Scope";
    	this.BLOCK_PATH = "simulink/Commonly Used Blocks/Scope";
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	}
		num++;
        // Initialize inputs and outputs if necessary
        this.inputs.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the block
        this.parameters.add(new SimulinkParameter<Integer>("NumInputPorts", this));
    }    
}

