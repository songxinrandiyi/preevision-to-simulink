package preevisiontosimulink.proxy.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mathworks.engine.*;

import preevisiontosimulink.library.*;
import preevisiontosimulink.proxy.block.ISimulinkBlock;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.relation.ISimulinkRelation;


public class SimulinkSubsystem implements ISimulinkSystem {
	private static final String BLOCK_NAME = "Subsystem";
	private static final String BLOCK_PATH = "simulink/Ports & Subsystems/Subsystem";
	private ISimulinkSystem parent;
	private String name;
	private static int num = 1;
	private List<LConnection> inConnections = new ArrayList<>();
	private List<RConnection> outConnections = new ArrayList<>();
	private List<Inport> inPorts = new ArrayList<>();
	private List<Outport> outPorts = new ArrayList<>();
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
	
	public Inport addInPort(Inport port) {
		inPorts.add(port);
		return port;
	}
	
	public Inport getInPort(int index) {
		return inPorts.get(index);
	}
	
	public Inport getInPort(String name) {
		for (Inport port : inPorts) {
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
	
	public List<Inport> getInPorts() {
		return inPorts;
	}
	
	public Outport addOutPort(Outport port) {
		outPorts.add(port);
		return port;
	}
	
	public Outport getOutPort(int index) {
		return outPorts.get(index);
	}
	
	public Outport getOutPort(String name) {
		for (Outport port : outPorts) {
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
	    return -1;  // or throw new IllegalArgumentException("Input port not found: " + name);
	}
	
	public List<Outport> getOutPorts() {
		return outPorts;
	}
	
	public LConnection addInConnection(LConnection port) {
		inConnections.add(port);
		return port;
	}
	
	public LConnection getInConnection(int index) {
		return inConnections.get(index);
	}
	
	public LConnection getInConnection(String name) {
		for (LConnection port : inConnections) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}
	
	public int getInConnectionIndex(String name) {
	    for (int i = 0; i < inConnections.size(); i++) {
	        if (inConnections.get(i).getName().equals(name)) {
	            return i+1;
	        }
	    }
	    // If the port with the given name is not found, return -1 or handle it as needed
	    return -1;  // or throw new IllegalArgumentException("Input port not found: " + name);
	}

	
	public List<LConnection> getInConnections() {
		return inConnections;
	}
	
	public RConnection addOutConnection(RConnection port) {
		outConnections.add(port);
		return port;
	}
	
	public RConnection getOutConnection(int index) {
		return outConnections.get(index);
	}
	
	public RConnection getOutConnection(String name) {
		for (RConnection port : outConnections) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}
	
	public int getOutConnectionIndex(String name) {
	    for (int i = 0; i < outConnections.size(); i++) {
	        if (outConnections.get(i).getName().equals(name)) {
	            return i+1;
	        }
	    }
	    // If the port with the given name is not found, return -1 or handle it as needed
	    return -1;  // or throw new IllegalArgumentException("Output port not found: " + name);
	}
	
	public List<RConnection> getOutConnections() {
		return outConnections;
	}	
	
	public String getConnectionPath(String name) {
	    for (int i = 0; i < inConnections.size(); i++) {
	        if (inConnections.get(i).getName().equals(name)) {
	        	int n = i+1;
	            return "LConn" + n;
	        }
	    }
	    for (int i = 0; i < outConnections.size(); i++) {
	        if (outConnections.get(i).getName().equals(name)) {
	        	int n = i+1;
	            return "RConn" + n;
	        }
	    }
	    return null;
	}
	
	public String getPortPath(String name) {
	    for (int i = 0; i < inPorts.size(); i++) {
	        if (inPorts.get(i).getName().equals(name)) {
	        	int n = i+1;
	            return "" + n;
	        }
	    }
	    for (int i = 0; i < outPorts.size(); i++) {
	        if (outPorts.get(i).getName().equals(name)) {
	        	int n = i+1;
	            return "" + n;
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
			for (LConnection port : inConnections) {
				port.generateModel(matlab);
			}
			
			// Generate the Simulink model for each RConnection in the outPorts
			for (RConnection port : outConnections) {
				port.generateModel(matlab);
			}
			
			// Generate the Simulink model for each port in the inPorts
			for (Inport port : inPorts) {
				port.generateModel(matlab);
			}
			
			// Generate the Simulink model for each port in the outPorts
			for (Outport port : outPorts) {
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
	
    private int extractNumber(String name) {
        String[] parts = name.split("_");
        return Integer.parseInt(parts[0]);
    }

    public void reorderConnections() {
        // Reorder inConnections
        Collections.sort(inConnections, new Comparator<LConnection>() {
            @Override
            public int compare(LConnection c1, LConnection c2) {
                return extractNumber(c1.getName()) - extractNumber(c2.getName());
            }
        });

        // Reorder outConnections
        Collections.sort(outConnections, new Comparator<RConnection>() {
            @Override
            public int compare(RConnection c1, RConnection c2) {
                return extractNumber(c1.getName()) - extractNumber(c2.getName());
            }
        });
    }
    
    public void reorderConnectionsRecursively(SimulinkSubsystem subsystem) {
        // Reorder connections for the current subsystem
        subsystem.reorderConnections();

        // Reorder connections for each child subsystem
        for (SimulinkSubsystem childSubsystem : subsystem.getSubsystemList()) {
            reorderConnectionsRecursively(childSubsystem);
        }
    }
}

