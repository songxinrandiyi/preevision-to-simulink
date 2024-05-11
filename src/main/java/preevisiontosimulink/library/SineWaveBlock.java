package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class SineWaveBlock extends SimulinkBlock {
	private static final String BLOCK_NAME = "SineWave";
	private static final String BLOCK_PATH = "simulink/Sources/Sine Wave";
    private static int num = 0;

    public SineWaveBlock(ISimulinkSystem parent, String name) {
		super(parent, name);
    }
    
    @Override
    public void initialize() {		
    	//Represents the number of instance created
    	num = num + 1;   	
		
        // Initialize inputs and outputs if necessary
        this.outputs.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the block
        this.parameters.add(new SimulinkParameter<Double>("Amplitude", this));
        this.parameters.add(new SimulinkParameter<Double>("Frequency", this));
		this.parameters.add(new SimulinkParameter<Double>("Bias", this));
        this.parameters.add(new SimulinkParameter<Double>("Phase", this));
    }
    
    @Override
    public void generateModel(MatlabEngine matlab) {
        try {
        	if(name == null) {
	        	name = BLOCK_NAME + num;
        	}
        	
        	String combinedPath = this.parent.getModelName() + "/" + name;
        	matlab.eval("add_block('" + BLOCK_PATH + "', '" + combinedPath + "')");
        	
        	System.out.println("Simulink block generated: " + combinedPath);
            for (SimulinkParameter<?> param : getParameters()) {
            	if(param.getValue() != null) {
            		matlab.eval("set_param('" + combinedPath + "', '" + param.getName() + "', '" + param.getValue().toString() + "')");
            		
					System.out.println("set_param('" + combinedPath + "', '" + param.getName() + "', '" + param.getValue().toString() + "')");
				} 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

