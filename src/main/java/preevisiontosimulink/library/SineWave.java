package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class SineWave extends SimulinkBlock {
    private static int num = 0;

    public SineWave(ISimulinkSystem parent, String name) {
    	super(parent, name);
    }
    
    @Override
    public void initialize() {	
    	this.BLOCK_NAME = "SineWave";
    	this.BLOCK_PATH = "simulink/Sources/Sine Wave";
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	}
		num++;
        // Initialize inputs and outputs if necessary
        this.outputs.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the block
        this.parameters.add(new SimulinkParameter<Double>("Amplitude", this));
        this.parameters.add(new SimulinkParameter<Double>("Frequency", this));
		this.parameters.add(new SimulinkParameter<Double>("Bias", this));
        this.parameters.add(new SimulinkParameter<Double>("Phase", this));
    }
}

