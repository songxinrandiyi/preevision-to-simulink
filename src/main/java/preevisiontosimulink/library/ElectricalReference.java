package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.port.LConn;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class ElectricalReference extends SimulinkBlock {
	private static int num = 1;

	public ElectricalReference(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "ElectricalReference";
		this.BLOCK_PATH = "fl_lib/Electrical/Electrical Elements/Electrical Reference";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		// Initialize inputs and outputs if necessary
		this.inPorts.add(new LConn(1, this));

		// Initialize parameters specific to the Sine Wave block
	}
}
