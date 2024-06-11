package preevisiontosimulink.library;

import java.util.ArrayList;
import com.mathworks.engine.*;
import preevisiontosimulink.proxy.*;
import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.SimulinkPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class SineWave extends SimulinkBlock {
	private static int num = 1;

	public SineWave(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "Sine";
		this.BLOCK_PATH = "simulink/Sources/Sine Wave";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;
		// Initialize inputs and outputs if necessary
		this.outPorts.add(new SimulinkPort(1, this));

		// Initialize parameters specific to the block
		this.parameters.add(new SimulinkParameter<Double>("Amplitude", this));
		this.parameters.add(new SimulinkParameter<Double>("Frequency", this));
		this.parameters.add(new SimulinkParameter<Double>("Bias", this));
		this.parameters.add(new SimulinkParameter<Double>("Phase", this));
	}
}
