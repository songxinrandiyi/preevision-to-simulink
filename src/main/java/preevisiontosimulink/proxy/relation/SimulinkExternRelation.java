package preevisiontosimulink.proxy.relation;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.port.ISimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;
import preevisiontosimulink.proxy.system.SimulinkSubsystem;

public class SimulinkExternRelation implements ISimulinkRelation {
    private ISimulinkPort outPort;
    private ISimulinkSystem parent;
	private String subsystemName;
	private String portName;

    public SimulinkExternRelation(ISimulinkPort outPort, String subsystemName, String portName, ISimulinkSystem parent) {
        this.subsystemName = subsystemName;
		this.portName = portName;
        this.outPort = outPort;
        this.parent = parent;
    }

    @Override
    public ISimulinkSystem getParent() {
        return parent;
    }

    @Override
    public void generateModel(MatlabEngine matlab) {
    	String destinationBlockPath = subsystemName + "/" + portName;
    	
        // Implementation for generating the Simulink relation 
        String sourceBlockPath = outPort.getParent().getName() + "/" + outPort.getName();

        String parentPath = parent.getName();
        ISimulinkSystem currentParent = parent.getParent();
        while (currentParent != null) {
            parentPath = currentParent.getName() + "/" + parentPath;
            currentParent = currentParent.getParent();
        }

        try {
            matlab.eval("add_line('" + parentPath + "', '" + sourceBlockPath + "', '" + destinationBlockPath + "', 'autorouting', 'on')");

            System.out.println("Simulink relation generated: " + sourceBlockPath + " -> " + destinationBlockPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
