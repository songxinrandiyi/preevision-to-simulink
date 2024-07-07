package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.port.LConnectionPort;
import preevisiontosimulink.proxy.port.RConnectionPort;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class CurrentSensor extends SimulinkBlock {
	private static int num = 1;

	public CurrentSensor(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "CurrentSensor";
		this.BLOCK_PATH = "fl_lib/Electrical/Electrical Sensors/Current Sensor";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;

		// Initialize inputs and outputs if necessary
		this.inPorts.add(new LConnectionPort(1, this));
		this.outPorts.add(new RConnectionPort(1, this));
		this.outPorts.add(new RConnectionPort(2, this));
	}
}
