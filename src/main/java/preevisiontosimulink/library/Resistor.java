package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.ContactPoint;
import preevisiontosimulink.proxy.port.LConnectionPort;
import preevisiontosimulink.proxy.port.RConnectionPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;


public class Resistor extends SimulinkBlock {
	private ContactPoint leftContactPoint;
	private ContactPoint rightContactPoint;

    private static int num = 1;

    public Resistor(ISimulinkSystem parent, String name) {
		super(parent, name);
    }    

	public ContactPoint getLeftContactPoint() {
		return leftContactPoint;
	}

	public void setLeftContactPoint(ContactPoint leftContactPoint) {
		this.leftContactPoint = leftContactPoint;
	}

	public ContactPoint getRightContactPoint() {
		return rightContactPoint;
	}

	public void setRightContactPoint(ContactPoint rightContactPoint) {
		this.rightContactPoint = rightContactPoint;
	}

	@Override
    public void initialize() {
    	this.BLOCK_NAME = "Resistor";
    	this.BLOCK_PATH = "fl_lib/Electrical/Electrical Elements/Resistor";
    	if(name == null) {
        	this.name = BLOCK_NAME + num;
    	}
		num++;
        // Initialize inputs and outputs if necessary
        this.inPorts.add(new LConnectionPort(1, this)); 
        this.outPorts.add(new RConnectionPort(1, this));

        // Initialize parameters specific to the Sine Wave block
        this.parameters.add(new SimulinkParameter<Double>("R", this));
    }
}

