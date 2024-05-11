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
    public ISimulinkBlock addBlock(ISimulinkBlock block) {
        blockList.add(block);
        return block;
    }

    @Override
    public SimulinkRelation addRelation(SimulinkRelation relation) {
        relationList.add(relation);
        return relation;
    }
    
	@Override
	public ISimulinkBlock getBlock(String name) {
		for (ISimulinkBlock block : blockList) {
			if (block.getName().equals(name)) {
				return block;
			}
		}
		return null;	
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
            
            // Generate the Simulink model for each relation in the relationList
            for (SimulinkRelation relation : relationList) {
                relation.generateModel(matlab);
            }
            
            matlab.eval("Simulink.BlockDiagram.arrangeSystem('" + modelName + "')");
            
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

