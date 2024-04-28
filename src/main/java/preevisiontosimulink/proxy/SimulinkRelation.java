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
    
    public void generateModel(MatlabEngine engine) {
    	
    }

}
