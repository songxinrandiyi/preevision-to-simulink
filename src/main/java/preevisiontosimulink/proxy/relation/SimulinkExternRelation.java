package preevisiontosimulink.proxy.relation;

import com.mathworks.engine.MatlabEngine;

import preevisiontosimulink.proxy.port.ISimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;
import preevisiontosimulink.proxy.system.ISubsystemInterface;

public class SimulinkExternRelation implements ISimulinkRelation {
    private ISubsystemInterface inPort;
    private ISimulinkPort outPort;
    private ISimulinkSystem parent;

    public SimulinkExternRelation(ISimulinkPort outPort, ISubsystemInterface inPort, ISimulinkSystem parent) {
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
}
