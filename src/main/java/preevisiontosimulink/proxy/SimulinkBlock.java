package preevisiontosimulink.proxy;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

public class SimulinkBlock implements ISimulinkBlock {
    protected String name;
    protected List<SimulinkPort> inputs = new ArrayList<>();
    protected List<SimulinkPort> outputs = new ArrayList<>();
    protected List<SimulinkParameter<?>> parameters = new ArrayList<>();
    protected ISimulinkSystem parent;
    
    public SimulinkBlock(ISimulinkSystem parent) {
        this.parent = parent;
        this.initialize();
    }
    
    @Override
    public List<SimulinkPort> getInputs() {
        return inputs;
    }
    
    @Override
    public List<SimulinkPort> getOutputs() {
        return outputs;
    }
       
	@Override
	public String getName() {
		return name;
	}

    @Override
    public void generateModel(MatlabEngine matlab) {

    }

    @Override
    public <T> void addParameter(SimulinkParameter<T> parameter) {
        parameters.add(parameter);
    }

    @Override
    public <T> void setParameter(String name, T value) {
        boolean found = false;
        for (SimulinkParameter<?> parameter : parameters) {
            if (parameter.getName().equals(name)) {
                @SuppressWarnings("unchecked")
                SimulinkParameter<T> typedParameter = (SimulinkParameter<T>) parameter;
                typedParameter.setValue(value);
                found = true;
                break;
            }
        }

        if (!found) {
            // Provide feedback when the parameter name is not found
            System.out.println("Parameter with name '" + name + "' not found in the SimulinkBlock.");
        }
    }
    
	@Override
	public List<SimulinkParameter<?>> getParameters() {
		return parameters;
	}

	@Override
	public void initialize() {
		
	}
}