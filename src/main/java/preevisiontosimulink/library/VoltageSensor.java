package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.LConn;
import preevisiontosimulink.proxy.port.RConn;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class VoltageSensor extends SimulinkBlock {
	private static int num = 1;

	public VoltageSensor(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	@Override
	public void initialize() {
		this.BLOCK_NAME = "VoltageSensor";
		this.BLOCK_PATH = "fl_lib/Electrical/Electrical Sensors/Voltage Sensor";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;

		// Initialize inputs and outputs if necessary
		this.inPorts.add(new LConn(1, this));
		this.outPorts.add(new RConn(1, this));
		this.outPorts.add(new RConn(2, this));

		this.parameters.add(new SimulinkParameter<String>("Orientation", this));
	}
}
