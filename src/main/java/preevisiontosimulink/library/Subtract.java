package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class Subtract extends SimulinkBlock {
	private static int num = 1;

	public Subtract(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Subtract";
		this.BLOCK_PATH = "simulink/Math Operations/Subtract";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		this.inPorts.add(new SimulinkPort(1, this));
		this.inPorts.add(new SimulinkPort(2, this));
		this.outPorts.add(new SimulinkPort(1, this));
		
		this.parameters.add(new SimulinkParameter<String>("Orientation", this));
	}
}
