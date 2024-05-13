package preevisiontosimulink.proxy;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

public class SimulinkSystem implements ISimulinkSystem {
	private ISimulinkSystem parent = null;
	private String name;
    private List<ISimulinkBlock> blockList = new ArrayList<>();
    private List<SimulinkRelation> relationList = new ArrayList<>();
    private List<SimulinkSubsystem> subsystemList = new ArrayList<>();
    
    public SimulinkSystem(String name) {
		this.name = name;
    }
    
    @Override
	public String getName() {
		return name;
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

    public void generateModel() {
        try {
            // Start the MATLAB engine
            MatlabEngine matlab = MatlabEngine.startMatlab();          
            // Generate the Simulink model (example)
            matlab.eval(name + " = new_system('" + name + "', 'Model')");
            
			// Generate the Simulink model for each subsystem in the subsystemList
			for (SimulinkSubsystem subsystem : subsystemList) {
				subsystem.generateModel(matlab);
			}
			
            // Generate the Simulink model for each block in the blockList
            for (ISimulinkBlock block : blockList) {
                block.generateModel(matlab);
            }
            
            // Generate the Simulink model for each relation in the relationList
            for (SimulinkRelation relation : relationList) {
                relation.generateModel(matlab);
            }
            
            matlab.eval("Simulink.BlockDiagram.arrangeSystem('" + name + "')");
            
            // Save the model
            String modelFilePath = "" + name + ".slx";
            matlab.eval("save_system('" + name + "', '" + modelFilePath + "')");

            System.out.println("Simulink model generated: " + name);

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
	public SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem) {
		subsystemList.add(subsystem);
		return subsystem;
	}

	@Override
	public ISimulinkSystem getParent() {
		return parent;
	}

	@Override
	public List<SimulinkSubsystem> getSubsystemList() {
		return subsystemList;
	}
}

