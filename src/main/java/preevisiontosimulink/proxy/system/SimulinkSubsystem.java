package preevisiontosimulink.proxy.system;

import java.util.ArrayList;
import java.util.List;

import com.mathworks.engine.*;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.relation.ISimulinkRelation;


public class SimulinkSubsystem implements ISimulinkSystem {
	private static final String BLOCK_NAME = "Subsystem";
	private static final String BLOCK_PATH = "simulink/Ports & Subsystems/Subsystem";
	private ISimulinkSystem parent;
	private String name;
	private static int num = 1;
	private List<LConnection> inPorts = new ArrayList<>();
	private List<RConnection> outPorts = new ArrayList<>();
    private List<ISimulinkBlock> blockList = new ArrayList<>();
    private List<ISimulinkRelation> relationList = new ArrayList<>();
    private List<SimulinkSubsystem> subsystemList = new ArrayList<>();
    
    public SimulinkSubsystem(ISimulinkSystem parent, String name) {
		this.parent = parent;
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	} else {
			this.name = name;
		}
		num++;
    }
    
	public ISimulinkSystem getParent() {
		return parent;
	}
	
	public LConnection addInPort(LConnection port) {
		inPorts.add(port);
		return port;
	}
	
	public LConnection getInPort(int index) {
		return inPorts.get(index);
	}
	
	public LConnection getInPort(String name) {
		for (LConnection port : inPorts) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}
	
	public int getInPortIndex(String name) {
	    for (int i = 0; i < inPorts.size(); i++) {
	        if (inPorts.get(i).getName().equals(name)) {
	            return i+1;
	        }
	    }
	    // If the port with the given name is not found, return -1 or handle it as needed
	    return -1;  // or throw new IllegalArgumentException("Input port not found: " + name);
	}

	
	public List<LConnection> getInPorts() {
		return inPorts;
	}
	
	public RConnection addOutPort(RConnection port) {
		outPorts.add(port);
		return port;
	}
	
	public RConnection getOutPort(int index) {
		return outPorts.get(index);
	}
	
	public RConnection getOutPort(String name) {
		for (RConnection port : outPorts) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}
	
	public int getOutPortIndex(String name) {
	    for (int i = 0; i < outPorts.size(); i++) {
	        if (outPorts.get(i).getName().equals(name)) {
	            return i+1;
	        }
	    }
	    // If the port with the given name is not found, return -1 or handle it as needed
	    return -1;  // or throw new IllegalArgumentException("Output port not found: " + name);
	}
	
	public List<RConnection> getOutPorts() {
		return outPorts;
	}	
	
	public String getPortPath(String name) {
	    for (int i = 0; i < inPorts.size(); i++) {
	        if (inPorts.get(i).getName().equals(name)) {
	        	int n = i+1;
	            return "LConn" + n;
	        }
	    }
	    for (int i = 0; i < outPorts.size(); i++) {
	        if (outPorts.get(i).getName().equals(name)) {
	        	int n = i+1;
	            return "RConn" + n;
	        }
	    }
	    return null;
	}
	
	@Override
	public String getName() {
		return name;
	}
           
	@Override
    public ISimulinkRelation addRelation(ISimulinkRelation relation) {
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

    public void generateModel(MatlabEngine matlab) {
        try {                	
            String combinedPath = generateCombinedPath();
        	
        	matlab.eval("add_block('" + BLOCK_PATH + "', '" + combinedPath + "')");
			matlab.eval("delete_line('" + combinedPath + "','In1/1','Out1/1')");
			matlab.eval("delete_block('" + combinedPath + "/In1')");
			matlab.eval("delete_block('" + combinedPath + "/Out1')");
            
			// Generate the Simulink model for each subsystem in the subsystemList
			for (SimulinkSubsystem subsystem : subsystemList) {
				subsystem.generateModel(matlab);
			}
			
			
            // Generate the Simulink model for each block in the blockList
            for (ISimulinkBlock block : blockList) {
                block.generateModel(matlab);
            }
            
			// Generate the Simulink model for each LConnection in the inPorts
			for (LConnection port : inPorts) {
				port.generateModel(matlab);
			}
			
			// Generate the Simulink model for each RConnection in the outPorts
			for (RConnection port : outPorts) {
				port.generateModel(matlab);
			}
            
            // Generate the Simulink model for each relation in the relationList
            for (ISimulinkRelation relation : relationList) {
                relation.generateModel(matlab);
            }
            
            matlab.eval("Simulink.BlockDiagram.arrangeSystem('" + combinedPath + "')");
            

            System.out.println("Simulink subsystem generated: " + combinedPath);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ISimulinkBlock> getBlockList() {
        return blockList;
    }

	@Override
    public List<ISimulinkRelation> getRelationList() {
        return relationList;
    }

	public SimulinkSubsystem addSubsystem(SimulinkSubsystem subsystem) {
		subsystemList.add(subsystem);
		return subsystem;
	}
	
	public String generateCombinedPath() {
	    StringBuilder pathBuilder = new StringBuilder(name);
	    ISimulinkSystem currentParent = parent;
	    while (currentParent != null) {
	        pathBuilder.insert(0, currentParent.getName() + "/");
	        currentParent = currentParent.getParent();
	    }
	    return pathBuilder.toString();
	}
		
	public List<SimulinkSubsystem> getSubsystemList() {
		return subsystemList;
	}

	@Override
	public ISimulinkBlock addBlock(ISimulinkBlock block) {
		blockList.add(block);
		return block;
	}

	@Override
	public SimulinkSubsystem getSubsystem(String name) {
		for (SimulinkSubsystem subsystem : subsystemList) {
			if (subsystem.getName().equals(name)) {
				return subsystem;
			}
		}
		return null;
	}
}

