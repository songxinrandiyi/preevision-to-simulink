package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class DCVoltageSource extends SimulinkBlock {
	private static final String BLOCK_NAME = "DCVoltageSource";
	private static final String BLOCK_PATH = "fl_lib/Electrical/Electrical Sources/DC Voltage Source";
    private static int num = 0;

    public DCVoltageSource(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

    @Override
    public void initialize() {
    	//Represents the number of instance created
    	num = num + 1;	   	
		
        // Initialize inputs and outputs if necessary
        this.outputs.add(new SimulinkPort(1, this)); 

        // Initialize parameters specific to the Sine Wave block
        this.parameters.add(new SimulinkParameter<Double>("v0", this));
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

