package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;
import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class Outport extends SimulinkBlock {

	private static int num = 1;

	public Outport(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Out";
		this.BLOCK_PATH = "simulink/Commonly Used Blocks/Out1";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;

		// Initialize inputs and outputs if necessary
		this.inPorts.add(new SimulinkPort(1, this));
	}
}
