package preevisiontosimulink.proxy;

import com.mathworks.engine.MatlabEngine;

public class SimulinkRelation {
    private ISimulinkPort inPort;
    private ISimulinkPort outPort;
    private ISimulinkSystem parent;

    public SimulinkRelation(ISimulinkPort inPort, ISimulinkPort outPort, ISimulinkSystem parent) {
        this.inPort = inPort;
        this.outPort = outPort;
        this.parent = parent;
    }

    public ISimulinkPort getInPort() {
        return inPort;
    }

    public ISimulinkPort getOutPort() {
        return outPort;
    }

    public ISimulinkSystem getParent() {
        return parent;
    }

    public void setInPort(ISimulinkPort inPort) {
        this.inPort = inPort;
    }

    public void setOutPort(ISimulinkPort outPort) {
        this.outPort = outPort;
    }
    
    public void generateModel(MatlabEngine matlab) {
		// Implementation for generating the Simulink relation 
    	String sourceBlockPath = inPort.getParent().getParent().getModelName() + "/" + inPort.getParent().getName();
    	String destinationBlockPath = outPort.getParent().getParent().getModelName() + "/" + outPort.getParent().getName();
    	String sourcePosition = "position_" + inPort.getParent().getName();
    	String destinationPosition = "position_" + outPort.getParent().getName();
    	String sourcePort = inPort.getIndex() + "";
		String destinationPort = outPort.getIndex() + "";
    	
    	try {
    		matlab.eval(sourcePosition + " = get_param('" + sourceBlockPath + "', 'PortConnectivity')");
			matlab.eval(destinationPosition + " = get_param('" + destinationBlockPath + "', 'PortConnectivity')");
            matlab.eval("add_line('" + parent.getModelName() + "', [" + sourcePosition + "(" + sourcePort + ").Position ; " + destinationPosition + "(" + destinationPort + ").Position])");

    		System.out.println("Simulink relation generated: " + sourceBlockPath + " -> " + destinationBlockPath);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
