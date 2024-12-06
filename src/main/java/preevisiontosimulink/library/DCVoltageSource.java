package preevisiontosimulink.library;

import preevisiontosimulink.proxy.block.SimulinkBlock;
import preevisiontosimulink.proxy.block.SimulinkParameter;
import preevisiontosimulink.proxy.port.LConn;
import preevisiontosimulink.proxy.port.RConn;
import preevisiontosimulink.proxy.system.ISimulinkSystem;

public class DCVoltageSource extends SimulinkBlock {
	private static int num = 1;

	public DCVoltageSource(ISimulinkSystem parent, String name) {
		super(parent, name);
	}

	//class DCVoltageSource extends SimulinkBlock
	@Override
	public void initialize() {
		this.BLOCK_NAME = "DCVoltageSource";
		this.BLOCK_PATH = "fl_lib/Electrical/Electrical Sources/DC Voltage Source";
		if (name == null) {
			this.name = BLOCK_NAME + num;
		}
		num++;

		this.inPorts.add(new LConn(1, this));
		this.outPorts.add(new RConn(1, this));
		this.parameters.add(new SimulinkParameter<Double>("v0", this));
	}
}
