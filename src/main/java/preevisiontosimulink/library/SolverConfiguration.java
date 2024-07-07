package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.RConnectionPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class SolverConfiguration extends SimulinkBlock {
	private static int num = 1;

	public SolverConfiguration(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Solver";
		this.BLOCK_PATH = "nesl_utility/Solver Configuration";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;

		// Initialize inputs and outputs if necessary
		this.inPorts.add(new RConnectionPort(1, this));

		// Initialize parameters specific to the Sine Wave block
		this.parameters.add(new SimulinkParameter<Double>("v0", this));
	}
}
