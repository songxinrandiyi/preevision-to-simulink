package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;


public class SineWaveBlock extends SimulinkBlock {
    private static final String BLOCK_NAME = "Sine";
    private static final String BLOCK_PATH = "simulink/Sources/Sine Wave";
    private static int num = 0;

    public SineWaveBlock(ISimulinkSystem parent) {
        super(parent);
    }

    @Override
    public void initialize() {
    	//Represents the number of instance created
    	num = num + 1;
        // Initialize inputs and outputs if necessary
        this.inputs = new ArrayList<>(); // Sine wave typically does not have inputs
        this.outputs = new ArrayList<>();
        this.outputs.add(new SimulinkPort(1, this)); // Assuming sine wave has one output

        // Initialize parameters specific to the Sine Wave block
        this.parameters = new ArrayList<>();
        this.parameters.add(new SimulinkParameter<Double>("Amplitude", this));
        this.parameters.add(new SimulinkParameter<Double>("Frequency", this));
        this.parameters.add(new SimulinkParameter<Double>("Phase", this));
    }

    @Override
    public void generateModel(MatlabEngine matlab) {
        try {
        	String combinedPath = this.parent.getModelName() + "/" + BLOCK_NAME + num;
        	matlab.eval("add_block('" + BLOCK_PATH + "', '" + combinedPath + "')");
            for (SimulinkParameter<?> param : getParameters()) {
            	if(param.getValue() != null) {
            		matlab.eval("set_param('" + combinedPath + "', '" + param.getName() + "', '" + param.getValue().toString() + "')");
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

