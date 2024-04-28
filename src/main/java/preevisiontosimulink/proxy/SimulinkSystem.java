package preevisiontosimulink.proxy;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

public class SimulinkSystem implements ISimulinkSystem {
	private String modelName;
    private List<ISimulinkBlock> blockList = new ArrayList<>();
    private List<SimulinkRelation> relationList = new ArrayList<>();
    
    public SimulinkSystem(String modelName) {
        this.modelName = modelName;
    }
    
    @Override
    public void addBlock(ISimulinkBlock block) {
        blockList.add(block);
    }

    @Override
    public void addRelation(SimulinkRelation relation) {
        relationList.add(relation);
    }


    @Override
    public <T> void addParameter(SimulinkParameter<T> parameter) {
        // Implementation for adding a parameter
    }

    @Override
    public void generateModel() {
        try {
            // Start the MATLAB engine
            MatlabEngine matlab = MatlabEngine.startMatlab();          
            // Generate the Simulink model (example)
            matlab.eval(modelName + " = new_system('" + modelName + "', 'Model')");
            // Generate the Simulink model for each block in the blockList
            for (ISimulinkBlock block : blockList) {
                block.generateModel(matlab);
            }
            /*
            matlab.eval("add_block('simulink/Sources/Sine Wave', '" + modelName + "/Sine')");
            matlab.eval("add_block('simulink/Math Operations/Gain', '" + modelName + "/Gain')");
            matlab.eval("add_block('simulink/Commonly Used Blocks/Scope', '" + modelName + "/Scope')");
            matlab.eval("add_line('" + modelName + "', 'Sine/1', 'Gain/1')");
            matlab.eval("add_line('" + modelName + "', 'Gain/1', 'Scope/1')");
            matlab.eval("set_param('" + modelName + "/Gain', 'Gain', '2')");
            */
            
            // Save the model
            String modelFilePath = "" + modelName + ".slx";
            matlab.eval("save_system('" + modelName + "', '" + modelFilePath + "')");

            System.out.println("Simulink model generated: " + modelName);

            // Close the MATLAB engine
            matlab.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ISimulinkBlock> getBlockList() {
        return blockList;
    }

    @Override
    public List<SimulinkRelation> getRelationList() {
        return relationList;
    }

	@Override
	public String getModelName() {
		return modelName;
	}
}

