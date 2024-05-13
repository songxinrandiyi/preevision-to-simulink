package preevisiontosimulink.proxy;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

public class SimulinkBlock implements ISimulinkBlock {
    protected String name;
    protected List<SimulinkPort> inputs;
    protected List<SimulinkPort> outputs;
    protected List<SimulinkParameter<?>> parameters;
    protected ISimulinkSystem parent;
	protected String BLOCK_NAME = "";
	protected String BLOCK_PATH = "";
    
    public SimulinkBlock(ISimulinkSystem parent, String name) {
		this.name = name;
        this.parent = parent;
        this.inputs = new ArrayList<>(); 
        this.outputs = new ArrayList<>();
		this.parameters = new ArrayList<>();
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
        try {
        	String combinedPath = generateCombinedPath();
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

	@Override
	public ISimulinkSystem getParent() {
		return parent;
	}
	
	@Override
	public String generateCombinedPath() {
	    StringBuilder pathBuilder = new StringBuilder(name);
	    ISimulinkSystem currentParent = parent;
	    while (currentParent != null) {
	        pathBuilder.insert(0, currentParent.getName() + "/");
	        currentParent = currentParent.getParent();
	    }
	    return pathBuilder.toString();
	}
}