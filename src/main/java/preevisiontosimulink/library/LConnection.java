package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.RConn;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class LConnection extends SimulinkBlock {
	private static int num = 1;

	public LConnection(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "LConn";
		this.BLOCK_PATH = "simulink/Signal Routing/Connection Port";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		// Initialize inputs and outputs if necessary
		this.inPorts.add(new RConn(1, this));

		// Initialize parameters specific to the Sine Wave block
		this.parameters.add(new SimulinkParameter<String>("Side", this));
		this.parameters.add(new SimulinkParameter<String>("Orientation", this));
	}
}
