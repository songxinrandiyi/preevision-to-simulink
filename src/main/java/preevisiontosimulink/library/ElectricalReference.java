package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class ElectricalReference extends SimulinkBlock {
	private static final String BLOCK_NAME = "ElectricalReference";
	private static final String BLOCK_PATH = "fl_lib/Electrical/Electrical Elements/Electrical Reference";
    private static int num = 0;

    public ElectricalReference(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

    @Override
    public void initialize() {
		//Represents the number of instance created
		num = num + 1;
		
        // Initialize inputs and outputs if necessary
        this.inputs.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the Sine Wave block
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


