package preevisiontosimulink.proxy;

import com.mathworks.engine.MatlabEngine;

public class SimulinkRelation {
    private ISimulinkBlock inBlock;
	private ISimulinkBlock outBlock;
	private int inPortIndex;
	private int outPortIndex;
    private ISimulinkSystem parent;

    public SimulinkRelation(ISimulinkBlock outBlock, ISimulinkBlock inBlock, int outPortIndex, int inPortIndex, ISimulinkSystem parent) {
        this.inBlock = inBlock;
		this.outBlock = outBlock;
		this.inPortIndex = inPortIndex;
		this.outPortIndex = outPortIndex;
        this.parent = parent;
    }

    public ISimulinkBlock getInBlock() {
        return inBlock;
    }

    public ISimulinkBlock getOutBlock() {
        return outBlock;
    }

    public ISimulinkSystem getParent() {
        return parent;
    }

    public void setInBlock(ISimulinkBlock inBlock) {
        this.inBlock = inBlock;
    }

    public void setOutBlock(ISimulinkBlock outBlock) {
        this.outBlock = outBlock;
    }
    
    public void generateModel(MatlabEngine matlab) {
		// Implementation for generating the Simulink relation 
    	String sourceBlockPath = outBlock.getName();
    	String destinationBlockPath = inBlock.getName();
    	ISimulinkSystem currentIn = inBlock.getParent();
		ISimulinkSystem currentOut = outBlock.getParent();
		while(currentOut != null) {
			sourceBlockPath = currentOut.getName() + "/" + sourceBlockPath;
			currentOut = currentOut.getParent();
		}
		
		while(currentIn != null) {
			destinationBlockPath = currentIn.getName() + "/" + destinationBlockPath;
			currentIn = currentIn.getParent();
		}
		
		String parentPath = parent.getName();
		ISimulinkSystem currentParent = parent.getParent();
		while(currentParent != null) {
			parentPath = currentParent.getName() + "/" + parentPath;
			currentParent = currentParent.getParent();
		}
    	
    	String sourcePosition = "position_" + outBlock.getName();
    	String destinationPosition = "position_" + inBlock.getName();
    	
    	try {
    		matlab.eval(sourcePosition + " = get_param('" + sourceBlockPath + "', 'PortConnectivity')");
			matlab.eval(destinationPosition + " = get_param('" + destinationBlockPath + "', 'PortConnectivity')");
            matlab.eval("add_line('" + parentPath + "', [" + sourcePosition + "(" + outPortIndex + ").Position ; " + destinationPosition + "(" + inPortIndex + ").Position])");

    		System.out.println("Simulink relation generated: " + sourceBlockPath + " -> " + destinationBlockPath);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
