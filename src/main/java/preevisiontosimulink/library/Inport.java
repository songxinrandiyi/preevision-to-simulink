package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;


public class Inport extends SimulinkBlock {

    public Inport(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

    @Override
    public void initialize() {
    	this.BLOCK_NAME = "In";
    	this.BLOCK_PATH = "simulink/Commonly Used Blocks/In1";

        // Initialize inputs and outputs if necessary
        this.outputs.add(new SimulinkPort(1, this)); 

        // Initialize parameters specific to the Sine Wave block
        this.parameters.add(new SimulinkParameter<Double>("R", this));
    }
}


