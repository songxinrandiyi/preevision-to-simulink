package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;
import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.LConnectionPort;
import preevisiontosimulink.proxy.port.RConnectionPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class DCVoltageSource extends SimulinkBlock {
	private static int num = 1;

	public DCVoltageSource(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "DCVoltageSource";
		this.BLOCK_PATH = "fl_lib/Electrical/Electrical Sources/DC Voltage Source";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;

		// Initialize inputs and outputs if necessary
		this.inPorts.add(new LConnectionPort(1, this));
		this.outPorts.add(new RConnectionPort(1, this));

		// Initialize parameters specific to the Sine Wave block
		this.parameters.add(new SimulinkParameter<Double>("v0", this));
	}
}
