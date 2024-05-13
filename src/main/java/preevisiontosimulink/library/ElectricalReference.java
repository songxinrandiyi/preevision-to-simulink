package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class ElectricalReference extends SimulinkBlock {
    private static int num = 0;

    public ElectricalReference(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

    @Override
    public void initialize() {
    	this.BLOCK_NAME = "ElectricalReference";
    	this.BLOCK_PATH = "fl_lib/Electrical/Electrical Elements/Electrical Reference";
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	}
		num++;
        // Initialize inputs and outputs if necessary
        this.inputs.add(new SimulinkPort(1, this));

        // Initialize parameters specific to the Sine Wave block
    }
}


