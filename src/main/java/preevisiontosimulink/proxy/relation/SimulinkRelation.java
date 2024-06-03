package preevisiontosimulink.proxy.relation;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.port.ISimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class SimulinkRelation implements ISimulinkRelation {
	private String name;
    private ISimulinkPort inPort;
    private ISimulinkPort outPort;
    private ISimulinkSystem parent;

    public SimulinkRelation(ISimulinkPort outPort, ISimulinkPort inPort, ISimulinkSystem parent) {
    	this.name = outPort.getParent().getName() + "_" + inPort.getParent().getName();
        this.inPort = inPort;
        this.outPort = outPort;
        this.parent = parent;
    }

    @Override
    public ISimulinkSystem getParent() {
        return parent;
    }

    @Override
    public void generateModel(MatlabEngine matlab) {
        // Implementation for generating the Simulink relation 
        String sourceBlockPath = outPort.getParent().getName() + "/" + outPort.getName();
        String destinationBlockPath = inPort.getParent().getName() + "/" + inPort.getName();

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

	@Override
	public String getName() {
		return name;
	}
}
