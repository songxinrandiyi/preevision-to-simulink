package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class GainBlock extends SimulinkBlock {
	private static final String BLOCK_NAME = "Gain";
	private static final String BLOCK_PATH = "simulink/Math Operations/Gain";
    private static int num = 0;

    public GainBlock(ISimulinkSystem parent) {
        super(parent);
    }    

    @Override
    public void initialize() {
    	//Represents the number of instance created
    	num = num + 1;	   	
		name = BLOCK_NAME + num;
		
        // Initialize inputs and outputs if necessary
        this.inputs = new ArrayList<>(); 
        this.outputs = new ArrayList<>();
		this.inputs.add(new SimulinkPort(1, this));
        this.outputs.add(new SimulinkPort(1, this)); 

        // Initialize parameters specific to the Sine Wave block
        this.parameters = new ArrayList<>();
        this.parameters.add(new SimulinkParameter<Double>("Gain", this));
    }
    
    @Override
    public void generateModel(MatlabEngine matlab) {
        try {
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

