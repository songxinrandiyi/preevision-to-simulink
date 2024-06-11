package preevisiontosimulink.library;

import preevisiontosimulink.proxy.*;
import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class Gain extends SimulinkBlock {
	private static int num = 1;

	public Gain(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Gain";
		this.BLOCK_PATH = "simulink/Math Operations/Gain";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		// Initialize inputs and outputs if necessary
		this.inPorts.add(new SimulinkPort(1, this));
		this.outPorts.add(new SimulinkPort(1, this));

		// Initialize parameters specific to the Sine Wave block
		this.parameters.add(new SimulinkParameter<Double>("Gain", this));
	}
}
