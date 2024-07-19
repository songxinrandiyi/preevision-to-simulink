package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class MathFunction extends SimulinkBlock {
	private static int num = 1;

	public MathFunction(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "MathFunction";
		this.BLOCK_PATH = "simulink/Math Operations/Math Function";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		this.inPorts.add(new SimulinkPort(1, this));
		this.outPorts.add(new SimulinkPort(1, this));

		// Initialize parameters specific to the Sine Wave block
		this.parameters.add(new SimulinkParameter<Double>("Operator", this));
	}
}
