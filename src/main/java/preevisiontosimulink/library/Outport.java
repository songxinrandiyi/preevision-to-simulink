package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;


public class Outport extends SimulinkBlock {

    public Outport(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

    @Override
    public void initialize() {
    	this.BLOCK_NAME = "Out";
    	this.BLOCK_PATH = "simulink/Commonly Used Blocks/Out1";
    	
        // Initialize inputs and outputs if necessary
        this.inputs.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the Sine Wave block
        this.parameters.add(new SimulinkParameter<Double>("R", this));
    }
}


