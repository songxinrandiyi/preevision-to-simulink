package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class Constant extends SimulinkBlock {
	private static int num = 1;

	public Constant(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Constant";
		this.BLOCK_PATH = "simulink/Sources/Constant";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		this.outPorts.add(new SimulinkPort(1, this));

		// Initialize parameters specific to the Sine Wave block
		this.parameters.add(new SimulinkParameter<Double>("Value", this));
		this.parameters.add(new SimulinkParameter<String>("Orientation", this));
	}
}
